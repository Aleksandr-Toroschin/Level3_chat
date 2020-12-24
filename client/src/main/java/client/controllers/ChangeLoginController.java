package client.controllers;

import client.models.Network;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.*;

public class ChangeLoginController {
    @FXML
    private TextField loginField;

    private Network network;

    private Stage stage;

    @FXML
    private void initialize() {
    }

    @FXML
    public void changeLogin() {

        String login = loginField.getText();
        if (!login.isEmpty()) {
            try {
                network.sendChangeLogin(login);
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.close();
        } else {
            System.out.println("Не заполнено значение поля Логин");
        }
    }

    @FXML
    public void cancel() {
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }
}
