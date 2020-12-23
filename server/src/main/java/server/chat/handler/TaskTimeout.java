package server.chat.handler;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskTimeout extends TimerTask {
    private ClientHandler handler;

    public TaskTimeout(ClientHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        if (!handler.isAction()) {
            handler.closeConnection(false);
            Logger logger = Logger.getGlobal();
            logger.log(Level.INFO, "Долгое бездействие, соединение разорвано");
//            System.out.println("Долгое бездействие, соединение разорвано");
        }
    }
}
