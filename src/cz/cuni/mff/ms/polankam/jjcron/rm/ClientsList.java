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
    private final ObservableList<String> activeClientsList;
    private final Map<String, ClientDetail> clientsDetailMap;

    public ClientsList() {
        activeClientsList = FXCollections.observableArrayList();;
        clientsDetailMap = new HashMap<>();
    }

    public ObservableList<String> getObservableList() {
        return activeClientsList;
    }

    public boolean isEmpty() {
        return clientsDetailMap.isEmpty();
    }

    public String addConnection(String addr, String id) {
        String concat = addr + "/" + id;
        activeClientsList.add(concat);
        clientsDetailMap.put(concat, new ClientDetail(addr, id));
        return concat;
    }

    public void deleteConnection(String mapId) {
        activeClientsList.remove(mapId);
        clientsDetailMap.remove(mapId);
    }

    public ClientDetail getConnection(String mapId) {
        return clientsDetailMap.get(mapId);
    }
}
