package cz.cuni.mff.ms.polankam.jjcron.rm;

import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Neloop
 */
public class ClientsList {

    private static final String ID_DELIMITER = "/";

    private final ObservableList<String> activeClientsList;
    private final Map<String, ClientHolder> clientsMap;

    public ClientsList() {
        activeClientsList = FXCollections.observableArrayList();;
        clientsMap = new HashMap<>();
    }

    public ObservableList<String> getObservableList() {
        return activeClientsList;
    }

    public boolean isEmpty() {
        return clientsMap.isEmpty();
    }

    public String addConnection(String addr, String id) {
        String concat = addr + ID_DELIMITER + id;
        if (!clientsMap.containsKey(concat)) {
            activeClientsList.add(concat);
            clientsMap.put(concat, new ClientHolder(addr, id));
        }

        return concat;
    }

    public void deleteConnection(String mapId) {
        activeClientsList.remove(mapId);
        clientsMap.remove(mapId);
    }

    public ClientHolder getConnection(String mapId) {
        return clientsMap.get(mapId);
    }
}
