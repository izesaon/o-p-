package com.example.cindy.op;

/**
 * Created by YiLong on 24/1/18.
 */

public class People {
    public double amount;
    public String name;
    public String contact;

    public People(){
        //Default constructor required for calls to DataSnapshot.getValue(People.class)
    }

    public People(double amount, String name, String contact){
        this.amount = amount;
        this.name = name;
        this.contact = contact;
    }

    public double getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    @Override
    public String toString(){
        return "Amount: "+this.amount+" Name: "+this.name+" Contact: "+this.contact;
    }
}
