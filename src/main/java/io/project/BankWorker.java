package io.project;

public class BankWorker extends User{
    String position;

    public BankWorker(int id, String account_type, String login, String password, String position) {
        super(id, account_type, login, password);
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
