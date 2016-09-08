package cz.cuni.mff.ms.polankam.jjcron.remote.manager;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author Neloop
 */
public class AlertDialogFactory {

    public Alert createConfirmationDialog(String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(message);
        alert.setContentText("Continue?");
        return alert;
    }

    public Alert createWarningDialog(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("Warning occured during execution");
        alert.setContentText(message);
        return alert;
    }

    public Alert createErrorDialog(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Error occured during execution");
        alert.setContentText(message);
        return alert;
    }
}
