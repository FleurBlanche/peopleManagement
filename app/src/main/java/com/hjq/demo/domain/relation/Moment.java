package com.hjq.demo.domain.relation;

import android.graphics.Bitmap;

import com.hjq.demo.domain.activity.SocialActivity;
import com.hjq.demo.domain.user.User;

import java.util.Comparator;

public class Moment {

    //朋友圈的动态
    private Long id = 1L;
    private String momenttime = "20191212";
    private String text = "PMas";
    private String image = "PMas";

    private Bitmap imageBMP;
    //额外保存用户
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMomenttime() {
        return momenttime;
    }

    public void setMomenttime(String momenttime) {
        this.momenttime = momenttime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Bitmap getImageBMP() {
        return imageBMP;
    }

    public void setImageBMP(Bitmap imageBMP) {
        this.imageBMP = imageBMP;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //排序
    public static class MomentComparetor implements Comparator<Moment> {
        @Override
        public int compare(Moment sc1, Moment sc2){
            if(sc1.getMomenttime().compareTo(sc2.getMomenttime()) < 0){
                return 1;
            }
            else if(sc1.getMomenttime().compareTo(sc2.getMomenttime()) == 0){
                return 0;
            }
            else{
                return -1;
            }
        }
    }
}
