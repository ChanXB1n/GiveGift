package com.example.chan.firstlevelproject;

/**
 * Created by Chan on 2018/1/1.
 */

public class ShowWishes {
    private String name;

    private String wishedText;

    private byte[] wishedImage;

    private String publishedTime;

    private String email;

    public String getWishedName(){
        return name;
    }


    public String getWishedText(){
        return wishedText;
    }


    public byte[] getWishedImage() {
        return wishedImage;
    }


    public String getPublishedTime(){
        return publishedTime;
    }

    public String getEmail(){return email;}

    public ShowWishes(String name,String wishedText,byte[] wishedImage,String publishedTime,String email){
        this.name=name;
        this.wishedText=wishedText;
        this.wishedImage=wishedImage;
        this.publishedTime=publishedTime;
        this.email=email;
    }

}
