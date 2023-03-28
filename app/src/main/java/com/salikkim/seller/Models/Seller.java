package com.salikkim.seller.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Seller implements Parcelable {
    private String S_Id, Name,Mobile, Alt_Mobile,Email,Address,Accounts;

    protected Seller(Parcel in) {
        S_Id = in.readString();
        Name = in.readString();
        Mobile = in.readString();
        Alt_Mobile = in.readString();
        Email = in.readString();
        Address = in.readString();
        Accounts = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(S_Id);
        dest.writeString(Name);
        dest.writeString(Mobile);
        dest.writeString(Alt_Mobile);
        dest.writeString(Email);
        dest.writeString(Address);
        dest.writeString(Accounts);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Seller> CREATOR = new Creator<Seller>() {
        @Override
        public Seller createFromParcel(Parcel in) {
            return new Seller(in);
        }

        @Override
        public Seller[] newArray(int size) {
            return new Seller[size];
        }
    };

    public String getS_Id() {
        return S_Id;
    }

    public String getName() {
        return Name;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getAlt_Mobile() {
        return Alt_Mobile;
    }

    public String getEmail() {
        return Email;
    }

    public String getAddress() {
        return Address;
    }

    public String getAccounts() {
        return Accounts;
    }
}
