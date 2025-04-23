package io.project.model;

public class Client extends User {
    private int id;
    private String name;
    private double salary;
    private String propertyType;
    private double propertyPrice;
    private int creditPeriod;
    private double creditAmount;
    private double balance;
    private double totalCredit;

    public Client(int id, String account_type, String login, String password) {
        super(id, account_type, login, password);
        this.name = login;
    }

    public Client(int id, String name, double salary, String propertyType, double propertyPrice,
                  int creditPeriod, double creditAmount, double balance){
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.propertyType = propertyType;
        this.propertyPrice = propertyPrice;
        this.creditPeriod = creditPeriod;
        this.balance = balance;
        this.creditAmount = creditAmount;
    }

    public Client(int id, String name, double salary, String propertyType, double propertyPrice,
                  int creditPeriod, double creditAmount, double balance, double totalCredit){
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.propertyType = propertyType;
        this.propertyPrice = propertyPrice;
        this.creditPeriod = creditPeriod;
        this.balance = balance;
        this.creditAmount = creditAmount;
        this.totalCredit = totalCredit;
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

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(double totalCredit) {
        this.totalCredit = totalCredit;
    }

    public double getPropertyPrice() {
        return propertyPrice;
    }

    public void setPropertyPrice(double propertyPrice) {
        this.propertyPrice = propertyPrice;
    }

    public int getCreditPeriod() {
        return creditPeriod;
    }

    public void setCreditPeriod(int creditPeriod) {
        this.creditPeriod = creditPeriod;
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
