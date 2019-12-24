package com.hjq.demo.domain.user;

import android.graphics.Bitmap;

public class Info {

    //保存用户的个人信息

    private Long id = 1L;
    private String name = "PMas";
    private String sex = "男";
    private String tag1 = "上海";
    private String tag2 = "IT";
    private String tag3 = "CEO";
    private String company = "sjtu";
    private String position = "PMas";
    private String mobile = "PMas";
    private String email = "PMas";
    private String address = "PMas";
    private String motto = "PMas";
    private String portrait = "PMas";
    private String background = "PMas";
    private Integer age = 20;

    private Bitmap portraitBMP;
    private Bitmap backgroundBMP;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Bitmap getPortraitBMP() {
        return portraitBMP;
    }

    public void setPortraitBMP(Bitmap portraitBMP) {
        this.portraitBMP = portraitBMP;
    }

    public Bitmap getBackgroundBMP() {
        return backgroundBMP;
    }

    public void setBackgroundBMP(Bitmap backgroundBMP) {
        this.backgroundBMP = backgroundBMP;
    }
}
