package cz.polankam.jjcron.remote.manager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Represents loading dialog screen which can be shared and used amongst whole
 * application. Loading dialog contains text which can be changed on dialog
 * show.
 *
 * @author Neloop
 */
public class LoadingScreen {

    /**
     * Default text which is displayed on loading dialog.
     */
    private static final String LOADING_TEXT = "Loading ...";
    /**
     * Size of loading circle.
     */
    private static final double LOADING_CIRCLE_SIZE = 70;
    /**
     * Size of whole loading dialog.
     */
    private static final double LOADING_DIALOG_SIZE = 150;

    /**
     * Loading dialog itself in form of JavaFX stage.
     */
    private final Stage loadingStage;
    /**
     * Label which contains editable loading text.
     */
    private final Label loadingText;

    /**
     * Construct whole loading dialog but do not show it.
     */
    public LoadingScreen() {
        loadingStage = new Stage(StageStyle.TRANSPARENT);

        ProgressIndicator loadingCircle = new ProgressIndicator(-1);
        BorderPane loadingLayout = new BorderPane();
        HBox textArea = new HBox();
        loadingText = new Label(LOADING_TEXT);

        loadingCircle.setMaxWidth(LOADING_CIRCLE_SIZE);
        loadingCircle.setMaxHeight(LOADING_CIRCLE_SIZE);

        textArea.setAlignment(Pos.CENTER);
        textArea.setPadding(new Insets(5));
        textArea.getChildren().add(loadingText);

        loadingLayout.setMinHeight(LOADING_DIALOG_SIZE);
        loadingLayout.setMinWidth(LOADING_DIALOG_SIZE);
        loadingLayout.setStyle("-fx-border-width: 2;"
                + "-fx-border-style: solid;"
                + "-fx-border-color: grey");
        loadingLayout.setCenter(loadingCircle);
        loadingLayout.setBottom(textArea);

        Scene scene = new Scene(loadingLayout, Color.TRANSPARENT);

        loadingStage.initModality(Modality.APPLICATION_MODAL);
        loadingStage.setAlwaysOnTop(true);
        loadingStage.setScene(scene);
    }

    /**
     * Sets primary stage which was given to application from JavaFX engine.
     * Primary stage is used for centering loading dialog, if given reference is
     * null than nothing is done.
     *
     * @param mainStage stage which is logical parent of loading screen
     */
    public void setMainStage(Stage mainStage) {
        if (mainStage == null) {
            return;
        }

        loadingStage.setOnShown((e) -> {
            loadingStage.setX(mainStage.getX() + (mainStage.getWidth() / 2)
                    - (loadingStage.getWidth() / 2));
            loadingStage.setY(mainStage.getY() + (mainStage.getHeight() / 2)
                    - (loadingStage.getHeight() / 2));
        });
    }

    /**
     * Shows loading dialog with default text.
     */
    public void show() {
        loadingStage.show();
    }

    /**
     * Shows loading dialog with given text.
     *
     * @param text text which will be visible on loading dialog
     */
    public void show(String text) {
        this.loadingText.setText(text);
        loadingStage.show();
    }

    /**
     * Hide loading dialog and resets default loading text.
     */
    public void hide() {
        loadingStage.hide();
        this.loadingText.setText(LOADING_TEXT);
    }
}
