package client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AboutController {


    @FXML
    private Button btnOk;

    private Stage aboutStage;

    @FXML
    private void initialize() {
    }

    public void setAboutStage(Stage aboutStage) {
        this.aboutStage = aboutStage;
    }

    @FXML
    private void handleOk() {
        aboutStage.close();
    }
}
