package com.hjq.demo.domain.relation;

import android.graphics.Bitmap;

import java.util.Comparator;

public class Record {

    //历史记录
    private Long id = 1L;
    private String recordtime = "20191212";
    private String text = "PMas";
    private String image = "PMas";

    private Bitmap imageBMP;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordtime() {
        return recordtime;
    }

    public void setRecordtime(String recordtime) {
        this.recordtime = recordtime;
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

    //排序
    public static class RecordComparetor implements Comparator<Record> {
        @Override
        public int compare(Record sc1, Record sc2){
            if(sc1.getRecordtime().compareTo(sc2.getRecordtime()) < 0){
                return 1;
            }
            else if(sc1.getRecordtime().compareTo(sc2.getRecordtime()) == 0){
                return 0;
            }
            else{
                return -1;
            }
        }
    }
}
