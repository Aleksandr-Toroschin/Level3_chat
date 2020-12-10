package client.models;

import client.NetworkClient;
import client.controllers.Controller;
import clientserver.Command;
import clientserver.commands.*;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;

public class Network {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8189;

    private final String serverHost;
    private final int serverPort;

    private ObjectInputStream dataInputStream;
    private ObjectOutputStream dataOutputStream;

    private Socket socket;

    private String userName;
    private String login;

    private boolean activeChat;

    private boolean isAction;

    public Network() {
        this(SERVER_HOST, SERVER_PORT);
    }

    private Network(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        isAction = false;
        activeChat = true;
    }

    public ObjectInputStream getDataInputStream() {
        return dataInputStream;
    }

    public ObjectOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public boolean connect() {
        try {
            socket = new Socket(serverHost, serverPort);
            dataOutputStream = new ObjectOutputStream(socket.getOutputStream());
            dataInputStream = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException e) {
            System.out.println("Не удалось установить соединение");
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        activeChat = false;
        if (socket != null && !socket.isClosed()) {
            try {
                dataOutputStream.writeObject(Command.endCommand());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    public void waitMessageFromServer(Controller controller) {
        Thread thread = new Thread(() -> {
            try {
                while (activeChat) {
                    Command command = readCommand();
                    if (command == null) {
                        System.out.println("Получена неопределенная команда");
                        //NetworkClient.showAlert("Ошибка сервера", "Получена неопределенная команда");
                        continue;
                    }
                    switch (command.getType()) {
                        case INFO_MESSAGE: {
                            MessageInfoCommandData data = (MessageInfoCommandData) command.getData();
                            String sender = data.getSender();
                            String message = data.getMessage();
                            String formatMessage = sender != null ? String.format("%s: %s", sender, message) : message;
                            Platform.runLater(() -> controller.addTextToList(formatMessage));
                            break;
                        }
                        case UPDATE_USERS_LIST: {
                            UpdateUsersListCommandData data = (UpdateUsersListCommandData) command.getData();
                            Platform.runLater(() -> controller.updateUsers(data.getUsers()));
                            break;
                        }
                        case ERROR: {
                            ErrorCommandData data = (ErrorCommandData) command.getData();
                            Platform.runLater(() -> NetworkClient.showAlert("Сообщение", data.getErrorMessage()));
                            break;
                        }
                        case END: {
                            activeChat = false;
                            break;
                        }
                        case CHANGE_LOGIN_OK: {
                            ChangeLoginOKCommandData data = (ChangeLoginOKCommandData) command.getData();
                            login = data.getNewLogin();
                            Platform.runLater(() -> NetworkClient.showAlert("Логин изменен", data.getNewLogin()));
                            break;
                        }
                        case CHANGE_LOGIN_ERR: {
                            ChangeLoginErrCommandData data = (ChangeLoginErrCommandData) command.getData();
                            Platform.runLater(() -> NetworkClient.showAlert("Не удалось изменить логин", data.getErrorMessage()));
                            break;
                        }
                        default: {
                            Platform.runLater(() -> NetworkClient.showAlert("Получена неизвестная команда", command.getType().toString()));
                        }
                    }
                }
            } catch (NullPointerException e) {
                System.out.println("Ошибка при обработке команды");
                e.printStackTrace();
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            } finally {
                isAction = false;
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            System.exit(0);
        });
        thread.setDaemon(true);
        thread.start();
    }

    public String sendAuthCommand(String login, String pw) {
        try {
            isAction = true;
            dataOutputStream.writeObject(Command.authCommand(login, pw));
            Command command = readCommand();
            if (command == null) {
                return "Не могу прочитать команду с сервера";
            }
            switch (command.getType()) {
                case AUTH_OK: {
                    AuthOkCommandData data = (AuthOkCommandData) command.getData();
                    this.userName = data.getUsername();
                    this.login = login;
                    return null;
                }
                case AUTH_ERROR:
                case ERROR: {
                    AuthErrorCommandData data = (AuthErrorCommandData) command.getData();
                    return data.getErrorMessage();
                }
                default: {
                    return "Неопознанный тип данных";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public void sendPrivateMessage(String message, String recipient) throws IOException {
        dataOutputStream.writeObject(Command.privateMessageCommand(recipient, message));
    }

    public void sendPublicMessage(String message) throws IOException {
        dataOutputStream.writeObject(Command.publicMessageCommand(userName, message));
    }

    public String getUserName() {
        return userName;
    }

    private Command readCommand() {
        try {
            return (Command) dataInputStream.readObject();
//            Object o = dataInputStream.readObject();
//
//            if (o instanceof Command) {
//                return (Command) o;
//            } else {
//                System.err.println("Получен неизвестный объект");
//            }
        } catch (ClassNotFoundException | IOException e) {
            System.err.println("Не удалось получить объект");
            e.printStackTrace();
            return null;
        }
    }

    public boolean isAction() {
        return isAction;
    }

    public void sendChangeLogin(String newLogin) throws IOException {
        dataOutputStream.writeObject(Command.changeLoginCommand(login, newLogin));
    }
}
