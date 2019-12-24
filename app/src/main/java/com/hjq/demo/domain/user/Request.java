package com.hjq.demo.domain.user;

import android.graphics.Bitmap;

public class Request {

    //请求
    private Long id = 1L;
    private String sendtime = "20191212";
    private String sender = "123";
    private String content = "PMaS";
    private String getter = "123";

    //发出请求的用户sender的两个信息
    private Bitmap portraitBMP;
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Bitmap getPortraitBMP() {
        return portraitBMP;
    }

    public void setPortraitBMP(Bitmap portraitBMP) {
        this.portraitBMP = portraitBMP;
    }

    public Long getId() {
        return id;
    }

    public String getSendtime() {
        return sendtime;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getGetter() {
        return getter;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }


}
