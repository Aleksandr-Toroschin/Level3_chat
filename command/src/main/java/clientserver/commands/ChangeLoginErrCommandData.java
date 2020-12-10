package clientserver.commands;

import java.io.Serializable;

public class ChangeLoginErrCommandData implements Serializable {

    private final String errorMessage;

    public ChangeLoginErrCommandData(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
