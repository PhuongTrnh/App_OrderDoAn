package com.example.store.bean;

import com.example.store.db.DatabaseHandler;

import java.io.Serializable;

public class Product implements Serializable {
    private int iID;
    private String sName;
    private int iIDCategory;
    private long lPrice;
    private String sDescription;
    private byte[] sSource;
    private int iQuantity;
    private int iState;


    public int getiID() {
        return iID;
    }
    public void setiID(int ID){this.iID = ID; }

    public String getsName(){
        return sName;
    }
    public void setsName(String Name){this.sName = Name; }

    public int getiIDCategory() {
        return iIDCategory;
    }
    public void setiIDCategory(int Category){this.iIDCategory = Category; }

    public long getlPrice() {
        return lPrice;
    }
    public void setlPrice(long Price){this.lPrice = Price; }

    public String getsDescription() {
        return sDescription;
    }
    public void setsDescription(String Description){this.sDescription = Description; }

    public byte[] getsSource() {
        return sSource;
    }
    public void setsSource(byte[] Src){this.sSource = Src; }
    public int getiQuantity() {
        return iQuantity;
    }
    public void setiQuantity(int quantity){this.iQuantity = quantity; }
    public int getiState() {
        return iState;
    }
    public void setiState(int state){this.iState = state; }


    public Product(){};

    public Product(int ID, String Name, int IDCategory, long Price, String Description, byte[] Source, int state){
        this.iID = ID;
        this.sName = Name;
        this.iIDCategory = IDCategory;
        this.lPrice = Price;
        this.sDescription = Description;
        this.sSource = Source;
        this.iState = state;
    }

    public Product(String Name, int IDCategory, long Price, String Description, byte[] Source, int quantity, int state){
        this.sName = Name;
        this.iIDCategory = IDCategory;
        this.lPrice = Price;
        this.sDescription = Description;
        this.sSource = Source;
        this.iQuantity = quantity;
        this.iState = state;
    }

    public void insertDefaultProduct(DatabaseHandler db){
        db.insertProduct(new Product("Tuna sandwich",1,22000,"It combine tuna, mayonnaise, celery, onion, relish, lemon juice, and garlic.",null, 10, 1));
        db.insertProduct(new Product("Shrimp sandwich",1,28000,"A loaded shrimp sandwich with a kick of heat and a double dose of avocado goodness!",null ,10 , 1));
        db.insertProduct(new Product("Chicken sandwich",1,25000,"A delicious mix of mayonnaise, chicken, pepper and some veggies spread on the bread",null,10, 1));
        db.insertProduct(new Product("Beef sandwich",1,27000,"A delicious mix of mayonnaise, chicken, pepper and some veggies spread on the bread",null,10, 0));
        db.insertProduct(new Product("Sausage sandwich",1,20000,"A delicious mix of mayonnaise, chicken, pepper and some veggies spread on the bread",null,10, 1));
        db.insertProduct(new Product("Coca Cola",2,7000,"Coca",null,20, 1));
        db.insertProduct(new Product("Orange Juice",2,15000,"Orange juice!",null,20, 1));
        db.insertProduct(new Product("Strawberry Smoothie",2,20000,"Strawberry",null,20, 0));
    }

    public Product(String sName, long lPrice, String sDescription, int iQuantity) {
        this.sName = sName;
        this.lPrice = lPrice;
        this.sDescription = sDescription;
        this.iQuantity = iQuantity;
        this.iIDCategory=1;
        this.iState=1;
    }

    public Product(String sName, long lPrice, String sDescription, int iQuantity, int IDCategory) {
        this.sName = sName;
        this.lPrice = lPrice;
        this.sDescription = sDescription;
        this.iQuantity = iQuantity;
        this.iIDCategory=IDCategory;
        this.iState=1;
    }

    public Product(String sName, long lPrice, String sDescription, int iQuantity, int IDCategory,byte[] sSource ) {
        this.sName = sName;
        this.lPrice = lPrice;
        this.sDescription = sDescription;
        this.iQuantity = iQuantity;
        this.iIDCategory=IDCategory;
        this.iState=1;
        this.sSource = sSource;
    }


    @Override
    public String toString() {
        return sName ;
    }
}
