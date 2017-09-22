package server.messages;

import java.io.Serializable;

/**
 * Created by sergz on 10.08.2017.
 */
public class Message implements Serializable{

    //ByFactFinalValues
    MessageType messageType;
    CommandType commandType;
    String body;

    String recipient;

    public void setSender(String sender) {
        this.sender = sender;
    }

    String sender;


   //For client

    public Message(MessageType messageType, CommandType commandType, String body, String recipient, String sender) {
        this.messageType = messageType;
        this.commandType = commandType;
        this.body = body;
        this.recipient = recipient;
        this.sender = sender;
    }

    public Message(MessageType messageType, String body, String recipient, String sender) {
        this.messageType = messageType;
        this.body = body;
        this.recipient = recipient;
        this.sender = sender;
    }


    public Message(MessageType messageType) {
        this.messageType = messageType;
    }



    @Override
    public String toString() {
        return "Message{" +
                "messageType=" + messageType +
                ", commandType=" + commandType +
                ", body='" + body + '\'' +
                ", recipient='" + recipient + '\'' +
                ", sender='" + sender + '\'' +
                '}';
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getBody() {
        return body;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }

}


