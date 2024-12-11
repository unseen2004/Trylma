package com.example.trylma;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private int playerId;

    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        new Thread(new ReadMessages()).start();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            out.println(input);
        }
    }

    private class ReadMessages implements Runnable {
        @Override
        public void run() {
            String response;
            try {
                while ((response = in.readLine()) != null) {
                    System.out.println(response);
                }
            } catch (IOException e) {
                System.out.println("Połączenie z serwerem zostało przerwane.");
            }
        }
    }

    public static void main(String[] args) {
        try {
            String host = "localhost";
            int port = 5000;
            Client client = new Client(host, port);
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
