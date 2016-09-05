package cz.cuni.mff.ms.polankam.jjcron.rm;

import java.util.Map;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Neloop
 */
public class ConnectionDetailPaneHolder {
    private Pane rootAnchorPane;
    private Pane upperPane;
    private Pane lowerPane;

    private final Map<String, ConnectionDetail> connectionsDetailMap;

    public ConnectionDetailPaneHolder(Map<String, ConnectionDetail> connDetails) {
        connectionsDetailMap = connDetails;
        initRootPane();
    }

    public Pane getRootPane() {
        return rootAnchorPane;
    }

    public void switchToConnectionDetail(String name) {
        ConnectionDetail conn = connectionsDetailMap.get(name);

        if (conn == null) {
            return;
        }

        upperPane.getChildren().clear();
        upperPane.getChildren().add(new Label(name));
    }

    private void initRootPane() {
        rootAnchorPane = new AnchorPane();
        VBox centerHBox = new VBox();
        upperPane = new GridPane();
        lowerPane = new GridPane();

        Label lab = new Label("Nothing to see here");
        Label lab1 = new Label("Here too");
        upperPane.getChildren().add(lab);
        lowerPane.getChildren().add(lab1);

        centerHBox.getChildren().addAll(upperPane, lowerPane);
        rootAnchorPane.getChildren().add(centerHBox);
    }
}
