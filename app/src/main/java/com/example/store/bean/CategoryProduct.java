package com.example.store.bean;

import java.io.Serializable;

import com.example.store.db.DatabaseHandler;

public class CategoryProduct implements Serializable {
    private int iID;
    private String sName;
    private byte[] sSource;

    public int getiID() {
        return iID;
    }

    public void setiID(int iID) {
        this.iID = iID;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String Name) {
        this.sName = Name;
    }

    public byte[] getsSource() {
        return sSource;
    }

    public void setsSource(byte[] Source) {
        this.sSource = Source;
    }

    public CategoryProduct(int ID, String Name, byte[] Source) {
        this.iID = ID;
        this.sName = Name;
        this.sSource = Source;
    }

    public CategoryProduct(String Name, byte[] Source) {
        this.sName = Name;
        this.sSource = Source;
    }

    public CategoryProduct(String Name) {
        this.sName = Name;
    }


    public CategoryProduct() {
    }

    ;

    public void insertDefaultCategory(DatabaseHandler db) {
        db.insertCategoryProduct(new CategoryProduct("Beverages", null));
        db.insertCategoryProduct(new CategoryProduct("Snacks", null));
        db.insertCategoryProduct(new CategoryProduct("Sanwiches", null));
    }

    @Override
    public String toString() {
        return sName;
    }
}
