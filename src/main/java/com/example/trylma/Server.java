package com.example.trylma;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();
    private int playerCount;
    private Board board;
    private int currentPlayerIndex = 0;

    public Server(int port, int playerCount) throws IOException {
        serverSocket = new ServerSocket(port);
        this.playerCount = playerCount;
        board = new Board(playerCount);
    }

    public void start() {
        System.out.println("Server started. Waiting for players...");
        while (clients.size() < playerCount) {
            try {
                Socket socket = serverSocket.accept();
                if (clients.size() >= playerCount) {
                    socket.close();
                    System.out.println("A player tried to join but the game is already full.");
                    continue;
                }
                ClientHandler clientHandler = new ClientHandler(socket, clients.size());
                clients.add(clientHandler);
                new Thread(clientHandler).start();
                System.out.println("Player " + clients.size() + " joined the game.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        broadcast("The game is starting!");
        sendToCurrentPlayer("Your move.");
    }

    private void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    private void sendToCurrentPlayer(String message) {
        clients.get(currentPlayerIndex).sendMessage(message);
    }

    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % playerCount;
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private int playerId;

        public ClientHandler(Socket socket, int playerId) {
            this.socket = socket;
            this.playerId = playerId;
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.println("Welcome to the game! Your number is: " + (playerId + 1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        @Override
        public void run() {
            if (clients.size() == playerCount) {
                startGame();
            }
            try {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (clients.get(currentPlayerIndex) == this) {
                        // Process move
                        String[] positions = inputLine.split(" ");
                        if (positions.length == 4) {
                            try {
                                int x1 = Integer.parseInt(positions[0]);
                                int y1 = Integer.parseInt(positions[1]);
                                int x2 = Integer.parseInt(positions[2]);
                                int y2 = Integer.parseInt(positions[3]);

                                synchronized (board) {
                                    if (board.movePiece(playerId, x1, y1, x2, y2)) {
                                        broadcast("Player " + (playerId + 1) + " made a move: (" + x1 + "," + y1 + ") -> (" + x2 + "," + y2 + ")");
                                        nextPlayer();
                                        sendToCurrentPlayer("Your move.");
                                    } else {
                                        out.println("Invalid move. Try again.");
                                        sendToCurrentPlayer("Your move.");
                                    }
                                }
                            } catch (NumberFormatException e) {
                                out.println("Invalid move format. Use: x1 y1 x2 y2");
                                sendToCurrentPlayer("Your move.");
                            }
                        } else {
                            out.println("Invalid move format. Use: x1 y1 x2 y2");
                            sendToCurrentPlayer("Your move.");
                        }
                    } else {
                        out.println("Wait for your turn.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void startGame() {
            if (playerId == 0) {
                sendMessage("Game started. Your move.");
            } else {
                sendMessage("Game started. Wait for your turn.");
            }
        }
    }

    public static void main(String[] args) {
        try {
            int port = 5000;
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the number of players (2, 3, 4, or 6): ");
            int playerCount = scanner.nextInt();
            Server server = new Server(port, playerCount);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InputMismatchException e) {
            System.out.println("Invalid number of players. Restart the program and enter an integer.");
        }
    }
}