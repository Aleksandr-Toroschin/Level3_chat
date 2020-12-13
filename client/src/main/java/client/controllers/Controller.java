package client.controllers;

import client.Contact;
import client.NetworkClient;
import client.models.Network;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Controller {
    private static final String aboutWindow = "/views/about.fxml";
    private static final String changeLoginWindow = "/views/changelogin.fxml";

    @FXML
    private TextField inputField;

    @FXML
    private TextArea messagesField;

    @FXML
    private ListView<String> usersList;

    @FXML
    private final List<String> users = new LinkedList<>();

    @FXML
    private final ObservableList<Contact> contacts = FXCollections.observableArrayList();

    private Network network;

    private String selectedRecipient;

    @FXML
    public void initialize() {
        usersList.setItems(FXCollections.observableArrayList(NetworkClient.TEST_USERS_STR));

        usersList.setCellFactory(lv -> {
            MultipleSelectionModel<String> selectionModel = usersList.getSelectionModel();
            ListCell<String> cell = new ListCell<>();
            cell.textProperty().bind(cell.itemProperty());
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                usersList.requestFocus();
                if (!cell.isEmpty()) {
                    int index = cell.getIndex();
                    if (selectionModel.getSelectedIndices().contains(index)) {
                        selectionModel.clearSelection(index);
                        selectedRecipient = null;
                    } else {
                        selectionModel.select(index);
                        selectedRecipient = cell.getItem();
                    }
                    event.consume();
                }
            });
            return cell;
        });
    }

    @FXML
    private void sendMessage() {
        String text = inputField.getText();
//        if (!text.isBlank()) {
        if (!text.equals("")) {
            inputField.clear();
            addTextToList("Я: " + text);

            try {
                if (selectedRecipient != null) {
                    network.sendPrivateMessage(text, selectedRecipient);
                } else {
                    network.sendPublicMessage(text);
                }
            } catch (IOException e) {
                e.printStackTrace();
                NetworkClient.showAlert("Не удалось отправить сообщение", e.getMessage());
            }
        }
    }

    @FXML
    public void addTextToList(String text) {
        String timeText = DateFormat.getInstance().format(new Date());
        addStringToList(timeText);
        addStringToList(text);
        network.addHistory(timeText);
        network.addHistory(text);
    }

    @FXML
    public void addStringToList(String text) {
        messagesField.appendText(text);
        messagesField.appendText(System.lineSeparator());
    }

    public void addHistory(List<String> history) {
        for (String s : history) {
            addStringToList(s);
        }
    }

    @FXML
    public void reset() {
        //messagesField.getItems().clear();
        messagesField.clear();
        inputField.setText("");
    }

    @FXML
    public void showAbout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(NetworkClient.class.getResource(aboutWindow));
            AnchorPane page = loader.load();

            Stage aboutStage = getStage(page, "О программе");

            AboutController controller = loader.getController();
            controller.setAboutStage(aboutStage);

            aboutStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void exit() {
        network.close();
        System.exit(0);
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void updateUsers(List<String> users) {
        users.remove(network.getUserName());  // удаляем самого себя
        usersList.setItems(FXCollections.observableArrayList(users));
    }

    @FXML
    public void showChangeLogin() {
        try {
            FXMLLoader loaderL = new FXMLLoader();
            loaderL.setLocation(NetworkClient.class.getResource(changeLoginWindow));
            AnchorPane page = loaderL.load();

            Stage chlStage = getStage(page, "Изменение логина");

            ChangeLoginController controller = loaderL.getController();
            controller.setStage(chlStage);
            controller.setNetwork(network);

            chlStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Stage getStage(AnchorPane page, String s) {
        Stage chlStage = new Stage();
        chlStage.setTitle(s);
        chlStage.initModality(Modality.WINDOW_MODAL);
//            chlStage.initOwner();
        Scene scene = new Scene(page);
        chlStage.setScene(scene);
        return chlStage;
    }
}
