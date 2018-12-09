package com.example.chan.firstlevelproject;

import org.litepal.crud.DataSupport;


public class Account extends DataSupport {

    private int id;

    private String name;

    private String password;

    private String email;

    private String phoneNumber;

    private byte[] headImage;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) { this.name=name;}

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email=email;}

    public String getPhoneNumber() {return phoneNumber;}

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber=phoneNumber;}

    public byte[] getHeadImage() {
        return headImage;
    }

    public void setHeadImage(byte[] headImage){
        this.headImage=headImage;
    }
}