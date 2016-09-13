package cz.polankam.jjcron.remote.manager;

import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Neloop
 */
public class ClientsHolder {

    /**
     *
     */
    public final ObservableList<String> clientsObservableList;
    /**
     *
     */
    private final Map<String, ClientWrapper> clientsMap;

    /**
     *
     */
    public ClientsHolder() {
        clientsMap = new HashMap<>();
        clientsObservableList = FXCollections.observableArrayList();
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return clientsMap.isEmpty();
    }

    /**
     *
     * @param client
     * @return
     */
    public String addClient(ClientWrapper client) {
        String id = client.getClientAddress().toString();
        if (!clientsMap.containsKey(id)) {
            clientsObservableList.add(id);
            clientsMap.put(id, client);
        }

        return id;
    }

    /**
     *
     * @param mapId
     */
    public void deleteClient(String mapId) {
        clientsObservableList.remove(mapId);
        clientsMap.remove(mapId);
    }

    /**
     *
     * @param mapId
     * @return
     */
    public ClientWrapper getClient(String mapId) {
        return clientsMap.get(mapId);
    }
}
