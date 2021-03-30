package cz.polankam.jjcron.remote.manager;

import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Holder class for all kind of collections which contains references to client
 * wrappers or other types of client representations.
 *
 * @author Neloop
 */
public class ClientsHolder {

    /**
     * Observable list which can be used in UI and is automatically updated.
     */
    public final ObservableList<String> clientsObservableList;
    /**
     * Uniquely indexed collection with all active clients.
     */
    private final Map<String, ClientWrapper> clientsMap;

    /**
     * By default empty collections are constructed.
     */
    public ClientsHolder() {
        clientsMap = new HashMap<>();
        clientsObservableList = FXCollections.observableArrayList();
    }

    /**
     * Determines if there are any active clients or not.
     *
     * @return true if collections are empty, false otherwise
     */
    public boolean isEmpty() {
        return clientsMap.isEmpty();
    }

    /**
     * Gets count of clients in this holder.
     *
     * @return integral value representing count
     */
    public int size() {
        return clientsMap.size();
    }

    /**
     * Add given client wrapper to all internal collections.
     * <p>
     * Has to be used in JavaFX UI thread.</p>
     *
     * @param client wrapper for client
     * @return unique identification of added client
     * @throws ManagerException if client was null
     */
    public String addClient(ClientWrapper client) throws ManagerException {
        if (client == null) {
            throw new ManagerException("ClientWrapper cannot be null");
        }

        String id = client.getClientAddress().toString();
        if (!clientsMap.containsKey(id)) {
            clientsObservableList.add(id);
            clientsMap.put(id, client);
        }

        return id;
    }

    /**
     * Delete client from all internal collections according to given
     * identification.
     * <p>
     * Has to be used in JavaFX UI thread.</p>
     *
     * @param id unique identification of client
     */
    public void deleteClient(String id) {
        clientsObservableList.remove(id);
        clientsMap.remove(id);
    }

    /**
     * Gets {@link ClientWrapper} class which implements all necessary methods
     * for working with client.
     *
     * @param id unique identification of client
     * @return client wrapper structure which simulates behaviour of client
     */
    public ClientWrapper getClient(String id) {
        return clientsMap.get(id);
    }
}
