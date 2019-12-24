package com.hjq.demo.domain.activity;

import android.graphics.Bitmap;

import java.util.Comparator;

public class SocialActivity {

    //服务器获取的社交活动
    private Long id = 1L;
    private String starttime = "20191212";
    private String endtime = "20191212";
    private String place = "PMas";
    private String name = "PMas";
    private String description = "PMas";
    private String tag = "";
    private String image = "PMas";
    private String sponsorid = "PMas";

    private Bitmap imageBMP;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSponsorid() {
        return sponsorid;
    }

    public void setSponsorid(String sponsorid) {
        this.sponsorid = sponsorid;
    }

    public Bitmap getImageBMP() {
        return imageBMP;
    }

    public void setImageBMP(Bitmap imageBMP) {
        this.imageBMP = imageBMP;
    }


    //排序
    public static class socialActivityComparetor implements Comparator<SocialActivity> {
        @Override
        public int compare(SocialActivity sc1, SocialActivity sc2){
            if(sc1.getStarttime().compareTo(sc2.getStarttime()) < 0){
                return 1;
            }
            else if(sc1.getStarttime().compareTo(sc2.getStarttime()) == 0){
                return 0;
            }
            else{
                return -1;
            }
        }
    }
}
