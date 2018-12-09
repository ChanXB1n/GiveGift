package com.example.chan.firstlevelproject;

import org.litepal.crud.DataSupport;


public class Wishes extends DataSupport {
    private String name;

    private String wishedText;

    private byte[] wishedImage;

    private String publishedTime;

    public String getWishedName(){
        return name;
    }

    public void setWishedName(String name){
        this.name=name;
    }

    public String getWishedText(){
        return wishedText;
    }

    public void setWishedText(String wishedText){
        this.wishedText=wishedText;
    }

    public byte[] getWishedImage() {
        return wishedImage;
    }

    public void setWishedImage(byte[] wishedImage){
        this.wishedImage=wishedImage;
    }

    public String getPublishedTime(){
        return publishedTime;
    }

    public void setPublishedTime(String publishedTime){ this.publishedTime=publishedTime; }
}
