package com.example.store.bean;

import java.io.Serializable;

import com.example.store.db.DatabaseHandler;

public class Role implements Serializable {
    private int iID;
    private String sName;

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

    public Role(int id, String Name){
        this.iID = id;
        this.sName = Name;
    }

    public Role(){}

    public void insertRole(DatabaseHandler db){
        try {
            db.insertRole(new Role(1, "Admin"));
            db.insertRole(new Role(2, "User"));
        }
        catch (Exception e){

        }
    }

    @Override
    public String toString() {
        return sName;
    }
}
