package server.messages;

import server.Client;
import server.Server;

import java.util.Random;
import java.util.Set;

/**
 * Created by sergz on 21.08.2017.
 */
public class MessageHandler {
    public void handleMessage(Message inputMessage) {
        //SIMPLE MESSAGE
        if (inputMessage.messageType == MessageType.MESSAGE) {
            Client recipient = Server.getClientById(inputMessage.recipient);
            recipient.postMessage(inputMessage);
        } else
            //UNREGISTER
            if (inputMessage.messageType == MessageType.UNREGISTER) {
                Server.unregisterClient(inputMessage.sender);
            } else
                //GETRANDOMID
                if (inputMessage.messageType == MessageType.COMMAND) {
                    if (inputMessage.commandType == CommandType.GETRANDOMID) {
                        Set<String> clients = Server.getClientsList();
                        Random random = new Random();
                        int index = random.nextInt(clients.size());
                        String foundID = "";
                        for (String clientId : clients) {
                            if (index == 0) {
                                foundID = clientId;
                            }
                            index--;
                        }
                        Message outputMessage = new Message(MessageType.COMMAND, CommandType.GETRANDOMID, foundID, inputMessage.getSender(), Server.getServerId());
                        System.out.println(foundID);
                        System.out.println("Sending response:   " + outputMessage);
                        Client recipient = Server.getClientById(inputMessage.getSender());
                        recipient.postMessage(outputMessage);
                    }
                    else if(inputMessage.commandType == CommandType.GETMYID){
                        Message outputMessage = new Message(MessageType.COMMAND, CommandType.GETMYID, inputMessage.getSender(), inputMessage.getSender(), Server.getServerId());
                        inputMessage.body = inputMessage.getSender();
                        Client recipient = Server.getClientById(inputMessage.getSender());
                        recipient.postMessage(outputMessage);
                    }
                }
    }
}
