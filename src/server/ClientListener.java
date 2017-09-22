package server;

import server.messages.Message;
import server.messages.MessageHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PushbackInputStream;
import java.net.SocketException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by sergz on 21.08.2017.
 */
public class ClientListener implements Runnable {
    private final List<Client> clients = new LinkedList<>();
    private final List<Client> clientsToAdd = new LinkedList<>();
    private MessageHandler messageHandler = new MessageHandler();

    void registerClient(Client client) {
        synchronized (clientsToAdd) {
            clientsToAdd.add(client);
        }
    }

    private void updateClients() {
        synchronized (clientsToAdd) {
            Iterator<Client> iterator = clientsToAdd.iterator();
            while (iterator.hasNext()) {
                clients.add(iterator.next());
                iterator.remove();
            }
        }
    }

    int getClientsCount() {
        synchronized (clientsToAdd) {
            return clients.size() + clientsToAdd.size();
        }
    }

    @Override
    public void run() {
        while (true) {
            Iterator<Client> iterator = clients.iterator();
            while (iterator.hasNext()) {
                Client client = iterator.next();
                if (client.isValid){
                    processClient(client);
                }
                else
                {
                    client.disconnect();
                    iterator.remove();
                }
            }
            updateClients();
        }
    }

    private void processClient(Client client) {
        System.out.println("Processing input...");
        processInputMessages(client);
        System.out.println("Processing output...");
        processOutputMessages(client);
    }

    private void processInputMessages(Client client) {
        if (!client.isValid){
            System.out.println("Client is invalid");
            return;
        }
        int existsFlag = -1;
        PushbackInputStream pushbackInputStream = client.pushbackInputStream;
        ObjectInputStream objectInputStream = client.objectInputStream;
        try {
//            if (objectInputStream.available()> 0 || (existsFlag = pushbackInputStream.read()) > -1) {
//                if (existsFlag > -1){
//                    pushbackInputStream.unread((byte) existsFlag);
//                }
                try {
                    Message message = (Message) objectInputStream.readObject();
                    message.setSender(client.clientID);
                    //System.out.println("thread: " + Thread.currentThread() + "   Client: " + client.clientID + "   " + message);
                    System.out.println("received "+message);
                    messageHandler.handleMessage(message);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("Unreadable message found....");
                }
//            } else {
//                System.out.println("No object found. Waiting. Client: " + client.clientID);
//                Thread.sleep(100);
//            }
        }
        catch (SocketException e){
            e.printStackTrace();
            client.invalidate();
        }
        catch (IOException  e) {
            client.invalidate();
            e.printStackTrace();
        }
    }

    private void processOutputMessages(Client client) {
        if (!client.isValid){
            System.out.println("Client is invalid");
            return;
        }
        Queue<Message> messages = client.getWaitingMessages();
        ObjectOutputStream objectOutputStream = client.getOutpustream();
        Iterator<Message> iterator = messages.iterator();

        while (iterator.hasNext()) {
            Message message = iterator.next();
            try {
                objectOutputStream.writeObject(message);
                objectOutputStream.flush();
                iterator.remove();
            }
            catch (SocketException e){
                e.printStackTrace();
                client.invalidate();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}