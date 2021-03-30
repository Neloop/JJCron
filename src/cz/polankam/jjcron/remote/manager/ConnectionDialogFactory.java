package cz.polankam.jjcron.remote.manager;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.util.Pair;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Factory used for creation of New connection dialog in which user should write
 * address of soon to be connected client.
 * <p>
 * Using of official JavaFX Dialogs, JDK 8u40 needed</p>
 *
 * @author Neloop
 */
public class ConnectionDialogFactory {

    /**
     * Creates and return New connection dialog which allows to specify registry
     * address and client identification.
     *
     * @return newly constructed dialog window with specified result
     */
    public Dialog<Pair<String, String>> createLoginDialog() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Connection Dialog");
        dialog.setHeaderText(
                "Please fill registry address and client identification");

        ButtonType connectButtonType = new ButtonType("Connect",
                ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(connectButtonType,
                ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField registryAddress = new TextField("//localhost");
        registryAddress.setPromptText("Registry URL");
        TextField clientIdentification = new TextField();
        clientIdentification.setPromptText("Client Identification");

        grid.add(new Label("Registry:"), 0, 0);
        grid.add(registryAddress, 1, 0);
        grid.add(new Label("Client Identification:"), 0, 1);
        grid.add(clientIdentification, 1, 1);

        // Login button is disabled by default
        Node connectButton
                = dialog.getDialogPane().lookupButton(connectButtonType);
        connectButton.setDisable(true);

        // Invalidate login button in case of empty strings in both fields
        connectButton.disableProperty().bind(
                Bindings.isEmpty(registryAddress.textProperty())
                .or(Bindings.isEmpty(clientIdentification.textProperty())));

        dialog.getDialogPane().setContent(grid);

        // Request focus on the registryAddress field by default
        Platform.runLater(() -> registryAddress.requestFocus());

        // Convert results to pair of server address and client identification
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == connectButtonType) {
                return new Pair<>(registryAddress.getText(),
                        clientIdentification.getText());
            }
            return null;
        });

        return dialog;
    }
}
