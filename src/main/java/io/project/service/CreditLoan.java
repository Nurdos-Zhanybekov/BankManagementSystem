package io.project.service;

public class CreditLoan {

    public Double countCreditSum(String propertyType, double price, int creditPeriod, double salary){
        double startingCreditInterest;
        if(propertyType.equalsIgnoreCase("apartment")){
            startingCreditInterest = 0.2;

            double changedInterestRateApartment = countInterestRate(startingCreditInterest, creditPeriod);
            price += price * changedInterestRateApartment;

            if(!canGetCredit(salary, price, creditPeriod)){
                return null;
            }

            return price;
        }else if(propertyType.equalsIgnoreCase("car")){
            startingCreditInterest = 0.15;
            double changedInterestRateCar = countInterestRate(startingCreditInterest, creditPeriod);
            price += price * changedInterestRateCar;

            if(!canGetCredit(salary, price, creditPeriod)){
                return null;
            }
            return price;
        }else{
            startingCreditInterest = 0.08;
            double changedInterestRateOthers = countInterestRate(startingCreditInterest, creditPeriod);
            price += price * changedInterestRateOthers;

            if(!canGetCredit(salary, price, creditPeriod)){
                return null;
            }

            return price;
        }
    }

    private double countInterestRate(double creditInterestRate, int period){
        if(period < 6){
            return creditInterestRate - 0.02;
        }

        if(period > 6){
            int increase_rate = period / 6;
            creditInterestRate += increase_rate * 0.02;
            return creditInterestRate;
        }

        return creditInterestRate;
    }

    public boolean canGetCredit(double salary, double creditSum, int period){
        return creditSum / period < salary;
    }
}
