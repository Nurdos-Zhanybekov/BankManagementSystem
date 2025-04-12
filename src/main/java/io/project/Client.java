package io.project;

public class Client extends User{
    private double salary;
    private double credit;
    int id;

    public Client(int id, String account_type, String login, String password, double salary, double credit) {
        super(id, account_type, login, password);
        this.salary = salary;
        this.credit = credit;
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

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }
}
