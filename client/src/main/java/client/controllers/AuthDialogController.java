package client.controllers;

import client.NetworkClient;
import client.models.Network;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthDialogController {

    @FXML
    public TextField loginField;

    @FXML
    public TextField pwField;

    @FXML
    public Button btnOk;

    @FXML
    public Button btnExit;

    private Network network;
    private NetworkClient networkClient;

    @FXML
    public void checkAuth(ActionEvent actionEvent) {
        String login = loginField.getText();
        String pw = pwField.getText();

//        if (login.isBlank() || pw.isBlank()) {
        if (login.equals("") || pw.equals("")) {
            NetworkClient.showAlert("Ошибка ввода данных", "Поля не должны быть пустыми");
            return;
        }

        String errorMessage = network.sendAuthCommand(login, pw);
        if (errorMessage == null) {
            networkClient.openChat();
        } else {
            NetworkClient.showAlert("Авторизация", errorMessage);
        }
    }

    @FXML
    public void exitApp(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setNetworkClient(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }
}
