package server;

/**
 * Created by sergz on 21.08.2017.
 */
public  class Balancer {
    private int availableCores = Runtime.getRuntime().availableProcessors() - 1;
    private int maxClientsForTrade = 20;
    private int threadsCount;
    private ClientListener[] listeners = new ClientListener[availableCores];



    public void registerClient(Client client) {
        boolean multiMode = true;
z
        boolean registered = false;
        for (int i = 0; i < threadsCount && !registered; i++) {
            if (listeners[i].getClientsCount() < maxClientsForTrade) {
                listeners[i].registerClient(client);
                registered = true;
            }
        }
        if (!registered) {
            if (threadsCount < availableCores) {
                ClientListener listener = new ClientListener();
                listeners[threadsCount] = listener;
                listener.registerClient(client);
                if (!multiMode){
                    listener.run();
                }else {
                    new Thread(listener).start();
                }
                threadsCount++;
            } else {
                maxClientsForTrade++;
                registerClient(client);
            }
        }
    }
}