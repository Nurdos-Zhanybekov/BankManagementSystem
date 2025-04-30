package io.project.model;

public class Client extends User {
    private int id;
    private String login;
    private double salarySom;
    private double salaryDollar;
    private double balanceSom;
    private double balanceDollar;

    public Client() {}

    public Client(int id, String login, String name){
        super();
        setId(id);
        setLogin(login);
        setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public double getBalanceSom() {
        return balanceSom;
    }

    public void setBalanceSom(double balanceSom) {
        this.balanceSom = balanceSom;
    }

    public double getBalanceDollar() {
        return balanceDollar;
    }

    public void setBalanceDollar(double balanceDollar) {
        this.balanceDollar = balanceDollar;
    }

    public double getSalarySom() {
        return salarySom;
    }

    public void setSalarySom(double salarySom) {
        this.salarySom = salarySom;
    }

    public double getSalaryDollar() {
        return salaryDollar;
    }

    public void setSalaryDollar(double salaryDollar) {
        this.salaryDollar = salaryDollar;
    }
}
