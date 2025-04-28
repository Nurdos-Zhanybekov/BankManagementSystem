package io.project.model;

public class Client extends User {
    private int id;
    private String login;
    private double salary;
    private double balance;

    public Client() {}


    public Client(int id, String login, String name, double salary){
        super();
        setId(id);
        setLogin(login);
        setName(name);
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
