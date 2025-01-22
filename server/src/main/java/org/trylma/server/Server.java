package org.trylma.server;

import org.trylma.server.game.network.ServerNetworkManager;

public class Server {
    private static final int DEFAULT_PORT = 12345;

    public static void main(String[] args) {
        ServerNetworkManager serverNetworkManager = new ServerNetworkManager(DEFAULT_PORT);
        serverNetworkManager.start();
    }
}