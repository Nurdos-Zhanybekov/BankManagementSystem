package io.project;

public class CreditLoan {

    public Double count_credit_sum(String property_type, double price, int credit_period, double salary){
        double starting_credit_interest;
        if(property_type.equalsIgnoreCase("apartment")){
            starting_credit_interest = 0.2;

            double changed_interest_rate_apartment = count_interest_rate(starting_credit_interest, credit_period);
            price += price * changed_interest_rate_apartment;

            if(!can_get_credit(salary, price, credit_period)){
                return null;
            }

            return price;
        }else if(property_type.equalsIgnoreCase("car")){
            starting_credit_interest = 0.15;
            double changed_interest_rate_car = count_interest_rate(starting_credit_interest, credit_period);
            price += price * changed_interest_rate_car;

            if(!can_get_credit(salary, price, credit_period)){
                return null;
            }
            return price;
        }else{
            starting_credit_interest = 0.08;
            double changed_interest_rate_others = count_interest_rate(starting_credit_interest, credit_period);
            price += price * changed_interest_rate_others;

            if(!can_get_credit(salary, price, credit_period)){
                return null;
            }

            return price;
        }
    }

    private double count_interest_rate(double credit_interest_rate, int period){
        if(period < 6){
            return credit_interest_rate - 0.02;
        }

        if(period > 6){
            int increase_rate = period / 6;
            credit_interest_rate += increase_rate * 0.02;
            return credit_interest_rate;
        }

        return credit_interest_rate;
    }

    public boolean can_get_credit(double salary, double credit_sum, int period){
        return credit_sum / period < salary;
    }
}
