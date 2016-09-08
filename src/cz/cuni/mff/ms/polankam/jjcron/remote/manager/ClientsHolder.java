package cz.cuni.mff.ms.polankam.jjcron.remote.manager;

import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Neloop
 */
public class ClientsHolder {

    public final ObservableList<String> clientsObservableList;
    private final Map<String, ClientHolder> clientsMap;

    public ClientsHolder() {
        clientsMap = new HashMap<>();
        clientsObservableList = FXCollections.observableArrayList();
    }

    public boolean isEmpty() {
        return clientsMap.isEmpty();
    }

    public String addClient(ClientHolder client) {
        String id = client.getClientAddress().toString();
        if (!clientsMap.containsKey(id)) {
            clientsObservableList.add(id);
            clientsMap.put(id, client);
        }

        return id;
    }

    public void deleteClient(String mapId) {
        clientsObservableList.remove(mapId);
        clientsMap.remove(mapId);
    }

    public ClientHolder getClient(String mapId) {
        return clientsMap.get(mapId);
    }
}
