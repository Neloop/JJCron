package cz.cuni.mff.ms.polankam.jjcron.rm;

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
 *
 * @author Neloop
 */
public class LoadingScreen {

    private static final String LOADING_TEXT = "Loading ...";
    private static final double LOADING_CIRCLE_SIZE = 70;
    private static final double LOADING_DIALOG_SIZE = 150;

    private final Stage loadingStage;
    private final Label loadingText;

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
        loadingLayout.setStyle("-fx-border-width: 2;" +
                "-fx-border-style: solid;" +
                "-fx-border-color: grey");
        loadingLayout.setCenter(loadingCircle);
        loadingLayout.setBottom(textArea);

        Scene scene = new Scene(loadingLayout, Color.TRANSPARENT);

        loadingStage.initModality(Modality.APPLICATION_MODAL);
        loadingStage.setAlwaysOnTop(true);
        loadingStage.setScene(scene);
    }

    public void show() {
        loadingStage.show();
    }

    public void show(String text) {
        this.loadingText.setText(text);
        loadingStage.show();
    }

    public void hide() {
        loadingStage.hide();
        this.loadingText.setText(LOADING_TEXT);
    }
}
