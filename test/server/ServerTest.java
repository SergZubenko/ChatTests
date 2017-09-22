package server;

import org.junit.Test;

/**
 * Created by sergz on 10.09.2017.
 */
public class ServerTest {
    @Test
    public void serverTestRun() throws Exception {
        new Server().runServer();
    }

}