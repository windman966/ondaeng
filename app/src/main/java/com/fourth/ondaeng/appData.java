package com.fourth.ondaeng;





import android.widget.TextView;





public class appData {


    public TextView header_name_tv;

    public static Object id="";
    public static Object dogName="";
    public static Object nickName = "";

    public static int point;


    private static appData me = new appData();

    private appData(){}
    public static appData getInstance() {
        return me;
    }

    public static Object getId() {
        return id;
    }

    public static Object getNickName() {
        return nickName;
    }

    public static void setNickName(Object nickName) {
        appData.nickName = nickName;
    }

    public void setId(Object id) {
        appData.id = id;
    }


    public static Object getDogName() {
        return dogName;
    }

    public static void setDogName(Object dogName) {
        appData.dogName = dogName;

    }
}
