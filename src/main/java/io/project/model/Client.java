package io.project.model;

public class Client extends User {
    private int id;
    private String name;
    private double salary;
    private double balance;

    public Client(int id, String account_type, String login, String password) {
        super(id, account_type, login, password);
        this.name = login;
    }

    public Client(){}

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
