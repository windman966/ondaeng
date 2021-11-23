package com.fourth.ondaeng;

import android.graphics.Bitmap;

public class dogIdCardData {
    String dogName;
    String birth;
    String registNo;
    String breed;
    String imgPath;

    Bitmap bm;
    //강아지 등록증을 위한 기초 데이터 구조
    public dogIdCardData(String dogName, String mobile, String registNo, String breed,Bitmap bm) {
        this.dogName = dogName;
        this.birth = mobile;
        this.registNo = registNo;
        this.breed = breed;
        this.bm = bm;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public void setBirth(String mobile) {
        this.birth = mobile;
    }

    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getDogName() {
        return dogName;
    }

    public String getBirth() {
        return birth;
    }

    public String getRegistNo() {
        return registNo;
    }

    public String getBreed() {
        return breed;
    }
}
