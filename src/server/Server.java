package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * Created by sergz on 21.08.2017.
 */
public class Server {
    private static final HashMap<String, Client> clients = new HashMap<>();
    private static final String serverID = "SERVER "+ UUID.randomUUID().toString();

    public static synchronized Client getClientById(String id) {
        return clients.get(id);
    }

    static synchronized void unregisterClient(Client client) {
        client.invalidate();
        clients.remove(client.clientID);
    }

    public static Set<String> getClientsList(){
        return clients.keySet();
    }

    public static String getServerId(){
        return serverID;
    }


    public static void unregisterClient(String clientId) {

       unregisterClient(getClientById(clientId));
    }z

    public static synchronized void registerClient(Client client) {
        clients.put(client.clientID, client);
    }


    public void runServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(3002);
        Balancer balancer = new Balancer();
        while (!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();
            System.out.println("Connected client " + socket);
            Client client = new Client(UUID.randomUUID().toString(), socket);
            registerClient(client);
            balancer.registerClient(client);
        }
    }

}
