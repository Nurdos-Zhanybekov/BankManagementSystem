package io.project;

public class Client extends User{
    private double salary;

    public Client(String account_type, String login, String password, double salary) {
        super(account_type, password, login);
        this.salary = salary;
    }

    public Client(String account_type, String login, String password){
        super(account_type, login, password);
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
