package client;

import javafx.beans.property.SimpleStringProperty;

public class Contact {
    private SimpleStringProperty name;

    public Contact(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
