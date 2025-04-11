package io.project;

public class Client extends User{
    private double salary;
    int id;

    public Client(String account_type, String login, String password, double salary) {
        super(account_type, password, login);
        this.salary = salary;
    }

    public Client(String account_type, String login, String password){
        super(account_type, login, password);
    }

    public Client(int id, String login){
        super(login);
        this.id = id;
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
}
