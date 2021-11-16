package com.fourth.ondaeng;

public class dogIdCardData {
    String dogName;
    String birth;
    String registNo;
    String breed;
    //강아지 등록증을 위한 기초 데이터 구조
    public dogIdCardData(String dogName, String mobile, String registNo, String breed) {
        this.dogName = dogName;
        this.birth = mobile;
        this.registNo = registNo;
        this.breed = breed;
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
