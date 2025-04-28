package io.project.model;

public class Credit {
    private String propertyType;
    private double propertyPrice;
    private int creditPeriod;
    private double propertyPriceWithCredit;
    private double totalCredit;

    public Credit(String propertyType, double propertyPrice, int creditPeriod, double propertyPriceWithCredit, double totalCredit) {
        this.propertyType = propertyType;
        this.propertyPrice = propertyPrice;
        this.creditPeriod = creditPeriod;
        this.propertyPriceWithCredit = propertyPriceWithCredit;
        this.totalCredit = totalCredit;
    }

    public Credit(String propertyType, double propertyPrice, int creditPeriod, double propertyPriceWithCredit){
        this.propertyType = propertyType;
        this.propertyPrice = propertyPrice;
        this.creditPeriod = creditPeriod;
        this.propertyPriceWithCredit = propertyPriceWithCredit;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
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

    public double getPropertyPriceWithCredit() {
        return propertyPriceWithCredit;
    }

    public void setPropertyPriceWithCredit(double propertyPriceWithCredit) {
        this.propertyPriceWithCredit = propertyPriceWithCredit;
    }

    public double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(double totalCredit) {
        this.totalCredit = totalCredit;
    }
}
