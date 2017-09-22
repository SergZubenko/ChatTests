package client;

import server.messages.CommandType;
import server.messages.Message;
import server.messages.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sergz on 10.09.2017.
 */
public class ChatClient {
    Socket socket;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    String id;
    final List<String> dialogs = new LinkedList<>();
    final List<Message> messages = new LinkedList<>();

    public void connect() throws IOException {
        socket = new Socket("localhost", 3002);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());

        ClientMessageHandler clientMessageHandler = new ClientMessageHandler();

        new Thread(() -> {
            while (true) {
                Message message = readMessage();
                messages.add(message);
                System.out.println("Response " + message);
                clientMessageHandler.processMessage(message);
        }
        }).start();

    }

    void disconnect() {
        try {
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            objectOutputStream = null;
        }
    }

    void sendMessage(Message message) {

        try {
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Message can't be processed");
        }

    }

    Message readMessage() {
        try {
            return (Message) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Can't read message ");
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    class ClientMessageHandler {
        void processMessage(Message message){
            if(message.getMessageType() == MessageType.MESSAGE){
                System.out.println(message);
            }else if(message.getMessageType() == MessageType.COMMAND) {
                if (message.getCommandType() == CommandType.GETMYID){
                    ChatClient.this.setId(message.getBody());
                }else if (message.getCommandType() ==  CommandType.GETRANDOMID){
                    dialogs.add(message.getBody());
                }
            }
        }

    }

}
