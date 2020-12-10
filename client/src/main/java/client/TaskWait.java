package client;

import client.models.Network;

import java.util.TimerTask;

public class TaskWait extends TimerTask {
    private Network network;

    public TaskWait(Network network) {
        this.network = network;
    }

    @Override
    public void run() {
        if (!network.isAction()) {
            network.close();
            System.out.println("Долгое бездействие, соединение разорвано");
        }
    }
}
