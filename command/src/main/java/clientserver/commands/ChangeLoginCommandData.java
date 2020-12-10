package clientserver.commands;

import java.io.Serializable;

public class ChangeLoginCommandData implements Serializable {
    private final String login;
    private final String loginNew;

    public ChangeLoginCommandData(String login, String loginNew) {
        this.login = login;
        this.loginNew = loginNew;
    }

    public String getLogin() {
        return login;
    }

    public String getLoginNew() {
        return loginNew;
    }
}
