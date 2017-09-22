package server;

import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

/**
 * Created by sergz on 13.09.2017.
 */

public class ServerEchoTest {


    @Test
    public void runServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(3002);
        while (!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();
            System.out.println("Connected client " + socket);
            Client client = new Client(UUID.randomUUID().toString(), socket);
            Server.registerClient(client);
            ClientListener clientListener = new ClientListener();
            clientListener.registerClient(client);
            clientListener.run();
        }

    }
}
