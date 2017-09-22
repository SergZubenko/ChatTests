package server.messages;

import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by sergz on 11.08.2017.
 */
public enum CommandType {
    SETNAME, SUBSCRIBE, UNSUBSCRIBE, GETMYID, GETRANDOMID;


}
