package client;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.messages.CommandType;
import server.messages.Message;
import server.messages.MessageType;

import java.io.IOException;

public class ClientTest {

    ChatClient chatClient = new ChatClient();

    @Before
    public void init() throws IOException {
        chatClient.connect();
    }

    @Test
    public void clientTestRun() throws IOException {
        for (int i = 0; i < 30; i++) {
            performDialog();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void performDialog() {
        Message response;
        Message message;


        //GET MY ID
        message = new Message(MessageType.COMMAND, CommandType.GETMYID, "", "", "");
        System.out.println("Sending   message " + message);
        chatClient.sendMessage(message);
        System.out.println("sent");


        //GET RANDOM ID
        message = new Message(MessageType.COMMAND, CommandType.GETRANDOMID, "", "", "");
        System.out.println("Sending   message " + message);
        chatClient.sendMessage(message);
        System.out.println("sent");



//        response = chatClient.readMessage();
//        System.out.println("Response  "+ response);



        //SEND MESSAGE TO RECEIVED ID
        while (chatClient.dialogs.size() == 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        message = new Message(MessageType.MESSAGE, "Test message  ", chatClient.getId(), chatClient.dialogs.get(0));
        System.out.println("Sending   message " + message);
        chatClient.sendMessage(message);
        System.out.println("sent");


//        response = chatClient.readMessage();
//        System.out.println("Response  "+ response);


//        message = new Message(MessageType.UNREGISTER);
//        System.out.println("Sending   message " + message);
//        chatClient.sendMessage(message);
//        System.out.println("sent");

    }


    //@Test
    public void send35Messages() {
        for (int i = 0; i < 35; i++) {
            Message message = new Message(MessageType.MESSAGE, "Test message  " + i, "", "");
            System.out.println("Sending " + i + "  message " + message);
            chatClient.sendMessage(message);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @After
    public void after() {
        chatClient.disconnect();
    }


}




