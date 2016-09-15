package cz.polankam.jjcron.remote.manager;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Factory class for all kinds of alert dialogs.
 *
 * @author Neloop
 */
public class AlertDialogFactory {

    /**
     * Create confirmation dialog with given message and returns it.
     *
     * @param message text which will be visible on dialog
     * @return alert dialog
     */
    public Alert createConfirmationDialog(String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(message);
        alert.setContentText("Continue?");
        return alert;
    }

    /**
     * Create warning dialog with given message and returns it.
     *
     * @param message text which will be visible on dialog
     * @return alert dialog
     */
    public Alert createWarningDialog(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("Warning occured during execution");
        alert.setContentText(message);
        return alert;
    }

    /**
     * Create error dialog with given message and returns it.
     *
     * @param message text which will be visible on dialog
     * @return alert dialog
     */
    public Alert createErrorDialog(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Error occured during execution");
        alert.setContentText(message);
        return alert;
    }
}
