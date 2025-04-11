package io.project;

public class BankWorker extends User{
    String position;

    public BankWorker(String account_type, String login, String password, String position) {
        super(account_type, login, password);
        this.position = position;
    }

    public BankWorker(String account_type, String login, String password) {
        super(account_type, login, password);
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
