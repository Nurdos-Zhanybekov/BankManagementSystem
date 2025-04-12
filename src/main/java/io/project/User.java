package io.project;

public class User {
    private String account_type;
    private String password;
    private String login;
    private int id;

    public User(int id, String account_type, String login, String password) {
        this.account_type = account_type;
        this.password = password;
        this.login = login;
        this.id = id;
    }

    public String getLogin() {
        return login;
    }
    public String getAccount_type() {
        return account_type;
    }

    public String getPassword() {
        return password;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
