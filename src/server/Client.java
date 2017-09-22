package server;

import server.messages.Message;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by sergz on 21.08.2017.
 */
public class Client {
    final String clientID;
    final Socket socket;
    Queue<Message> outputMessagesQueue;
    PushbackInputStream pushbackInputStream;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    public boolean isValid;

    public Client(String cliendID, Socket socket) {
        this.clientID = cliendID;
        this.socket = socket;
        this.isValid = true;

        initStreams();
        outputMessagesQueue = new LinkedList<>();
    }

    private void initStreams(){
        try {
            pushbackInputStream = new PushbackInputStream(socket.getInputStream());
            objectInputStream = new ObjectInputStream(new BufferedInputStream(pushbackInputStream));
            objectOutputStream  = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e)
        {   this.isValid = false;
            e.printStackTrace();
        }
    }

    public void closeStreams(){

        try {
            pushbackInputStream.close();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectOutputStream getOutpustream() {
        return objectOutputStream;
    }

    public Queue<Message> getWaitingMessages() {
        return outputMessagesQueue;
    }

    public void postMessage(Message message){
        outputMessagesQueue.add(message);
    }

    public void disconnect() {
        closeStreams();
    }

    public void invalidate() {
        isValid = false;
    }
}