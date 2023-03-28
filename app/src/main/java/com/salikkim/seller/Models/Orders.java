package com.salikkim.seller.Models;

public class Orders {
    private String Title,Thumbnail,Screenshot,Color, Size, Date,Desc,Status_Info,Customer_Name,Customer_Mobile,Alt_Mobile,Address;
    private int O_Id,S_id,Status,Quantity;
    private double Price,Sale_Price,Shipping_Charge,Commission;

    public String getTitle() {
        return Title;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public String getScreenshot() {
        return Screenshot;
    }

    public String getColor() {
        return Color;
    }

    public String getSize() {
        return Size;
    }

    public String getDate() {
        return Date;
    }

    public String getDesc() {
        return Desc;
    }

    public String getStatus_Info() {
        return Status_Info;
    }

    public String getCustomer_Name() {
        return Customer_Name;
    }

    public String getCustomer_Mobile() {
        return Customer_Mobile;
    }

    public String getAlt_Mobile() {
        return Alt_Mobile;
    }

    public String getAddress() {
        return Address;
    }

    public int getO_Id() {
        return O_Id;
    }

    public int getS_id() {
        return S_id;
    }

    public int getStatus() {
        return Status;
    }

    public int getQuantity() {
        return Quantity;
    }

    public double getPrice() {
        return Price;
    }

    public double getSale_Price() {
        return Sale_Price;
    }

    public double getShipping_Charge() {
        return Shipping_Charge;
    }

    public double getCommission() {
        return Commission;
    }
}
