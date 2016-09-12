package cz.cuni.mff.ms.polankam.jjcron.remote.manager;

import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.StageStyle;

/**
 *
 * @author Neloop
 */
public class AboutDialogFactory {

    private static final String BOLD_TEXT_CSS = "-fx-font-weight: bold";
    private static final String CONTRIBUTORS = "Martin Polanka";

    private static final double STANDARD_PADDING = 10;
    private static final double WIDTH = 400;

    /**
     *
     */
    private final AppInfo appInfo;
    /**
     *
     */
    private final HostServices hostServices;

    /**
     *
     * @param appInfo
     * @param hostServices
     */
    public AboutDialogFactory(AppInfo appInfo, HostServices hostServices) {
        this.appInfo = appInfo;
        this.hostServices = hostServices;
    }

    /**
     *
     * @return
     */
    public Dialog createAboutDialog() {
        Dialog dialog = new Dialog();
        dialog.initStyle(StageStyle.UTILITY);

        ButtonType closeButtonType = new ButtonType("Close", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(closeButtonType);

        ImageView logo = new ImageView("/cz/cuni/mff/ms/polankam/jjcron/common/logo.png");
        logo.setFitWidth(WIDTH);

        TextFlow description = new TextFlow();
        description.setMaxWidth(WIDTH);
        Text basicDesc = new Text("JJCronRM is management UI application for multiple instances of cron-like program JJCron.");
        description.getChildren().addAll(basicDesc);

        TextFlow moreInfo = new TextFlow();
        Hyperlink moreLink = new Hyperlink(appInfo.sourceLinkText);
        moreLink.setPadding(Insets.EMPTY);
        moreLink.setOnAction((e) -> {
            hostServices.showDocument(appInfo.sourceLink);
        });
        Text moreBefore = new Text("For more information, please see ");
        Text moreAfter = new Text(".");
        moreInfo.getChildren().addAll(moreBefore, moreLink, moreAfter);

        TextFlow version = new TextFlow();
        Text versionTitle = new Text("Version: ");
        versionTitle.setStyle(BOLD_TEXT_CSS);
        Text versionText = new Text(getClass().getPackage().getImplementationVersion());
        version.getChildren().addAll(versionTitle, versionText);

        TextFlow source = new TextFlow();
        Text sourceTitle = new Text("Source: ");
        sourceTitle.setStyle(BOLD_TEXT_CSS);
        Hyperlink sourceLink = new Hyperlink(appInfo.sourceLink);
        sourceLink.setPadding(Insets.EMPTY);
        sourceLink.setOnAction((e) -> {
            hostServices.showDocument(appInfo.sourceLink);
        });
        source.getChildren().addAll(sourceTitle, sourceLink);

        TextFlow license = new TextFlow();
        Text licenseTitle = new Text("License: ");
        licenseTitle.setStyle(BOLD_TEXT_CSS);
        license.getChildren().addAll(licenseTitle, new Text("MIT"));

        TextFlow contributors = new TextFlow();
        Text contributorsTitle = new Text("Contributors: ");
        contributorsTitle.setStyle(BOLD_TEXT_CSS);
        contributors.getChildren().addAll(contributorsTitle, new Text(CONTRIBUTORS));

        TextFlow runtime = new TextFlow();
        Text runtimeTitle = new Text("Runtime: ");
        runtimeTitle.setStyle(BOLD_TEXT_CSS);
        Text runtimeVendor = new Text(System.getProperty("java.runtime.name"));
        Text runtimeDelim = new Text("; ");
        Text runtimeVersion = new Text(System.getProperty("java.version"));
        runtime.getChildren().addAll(runtimeTitle, runtimeVendor, runtimeDelim, runtimeVersion);

        VBox details = new VBox();
        details.setPadding(new Insets(5));
        details.setStyle("-fx-border-color: grey;"
                + "-fx-border-width: 1;"
                + "-fx-background-color: white;");
        details.setMinWidth(WIDTH);
        details.getChildren().addAll(source, version, license, contributors, runtime);

        VBox detailsArea = new VBox();
        detailsArea.setPadding(new Insets(STANDARD_PADDING, 0, 0, 0));
        detailsArea.getChildren().addAll(details);

        VBox content = new VBox();
        content.getChildren().addAll(logo, description, moreInfo, detailsArea);
        dialog.getDialogPane().setContent(content);

        return dialog;
    }
}
