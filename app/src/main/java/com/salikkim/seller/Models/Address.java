package com.salikkim.seller.Models;

public class Address {
    private int Id;
    private String Name;
    private boolean isCheck;
    private double Charge;

    public Address() {
    }

    public Address(int id, String name, boolean isCheck, double charge) {
        Id = id;
        Name = name;
        this.isCheck = isCheck;
        Charge = charge;
    }

    public double getCharge() {
        return Charge;
    }

    public void setCharge(double charge) {
        Charge = charge;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
