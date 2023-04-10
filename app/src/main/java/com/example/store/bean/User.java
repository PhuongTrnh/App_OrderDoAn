package com.example.store.bean;

import java.io.Serializable;

public class User implements Serializable {
    private int iID;
    private String sName;
    private String sEmail;
    private String sPhone;
    private String sPassword;
    private int iVerifyEmail;
    private int iState;
    private int iRole;
    private byte[] sSource;
    private String sAddress;

    public int getiID() {
        return iID;
    }
    public void setiID(int id) {
        this.iID = id;
    }

    public String getsName() {
        return sName;
    }
    public void setsName(String name) {
        this.sName = name;
    }

    public String getsEmail() {
        return sEmail;
    }
    public void setsEmail(String email) {
        this.sEmail = email;
    }

    public String getsPhone() {
        return sPhone;
    }
    public void setsPhone(String phone) {
        this.sPhone = phone;
    }

    public String getsPassword() {
        return sPassword;
    }
    public void setsPassword(String password) {
        this.sPassword = password;
    }

    public int getiVerifyEmail(){
        return iVerifyEmail;
    };
    public void setbVerifyEmail(int verifyemail){
        this.iVerifyEmail = verifyemail;
    };

    public int getiState(){
        return iState;
    };
    public void setbState(int state){
        this.iState = state;
    };

    public int getiRole() {
        return iRole;
    }
    public void setiRole(int role) {
        this.iRole = role;
    }

    public byte[] getsSource() {
        return sSource;
    }
    public void setsSource(byte[] Src){this.sSource = Src; }

    public String getsAddress(){return sAddress;}
    public void setsAddress(String address){this.sAddress = address;}

    public User(){};

    public User(int id,String Name, String Email, String Phone, String Password, int VerifyEmail, int State, int Role, byte[] source, String address){
        this.iID = id;
        this.sName = Name;
        this.sEmail = Email;
        this.sPhone = Phone;
        this.sPassword = Password;
        this.iVerifyEmail = VerifyEmail;
        this.iState = State;
        this.iRole = Role;
        this.sSource = source;
        this.sAddress = address;
    }

    public User(String Name, String Email, String Phone, String Password, int VerifyEmail, int State, int Role, byte[] source){
        this.sName = Name;
        this.sEmail = Email;
        this.sPhone = Phone;
        this.sPassword = Password;
        this.iVerifyEmail = VerifyEmail;
        this.iState = State;
        this.iRole = Role;
        this.sSource = source;
    }

    public User(String Name, String Email, String Address, String Phone, int Role, byte[] source){
        this.sName = Name;
        this.sEmail = Email;
        this.sPhone = Phone;
        this.sPassword = "12345678";
        this.iVerifyEmail = 1;
        this.iState = 1;
        this.iRole = Role;
        this.sSource = source;
        this.sAddress = Address;
    }
    public User(String Name, String Email, String Address, String Phone, int Role){
        this.sName = Name;
        this.sEmail = Email;
        this.sPhone = Phone;
        this.sPassword = "12345678";
        this.iVerifyEmail = 1;
        this.iState = 1;
        this.iRole = Role;
        this.sAddress = Address;
    }


    public User(String Name, String Email, String Phone, String Password, int VerifyEmail, int State, int Role, byte[] source, String address){
        this.sName = Name;
        this.sEmail = Email;
        this.sPhone = Phone;
        this.sPassword = Password;
        this.iVerifyEmail = VerifyEmail;
        this.iState = State;
        this.iRole = Role;
        this.sSource = source;
        this.sAddress = address;
    }

    public User(int ID, String Name, String Email, String Phone, int State, int Role, String address){
        this.iID = ID;
        this.sName = Name;
        this.sEmail = Email;
        this.sPhone = Phone;
        this.iState = State;
        this.iRole = Role;
        this.sAddress = address;
    }

    @Override
    public String toString() {
        return sEmail;
    }
}