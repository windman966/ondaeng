package com.fourth.ondaeng;





import android.widget.TextView;





public class appData {


    public TextView header_name_tv;

    public static Object id="";
    public static Object dogName="";


    private static appData me = new appData();

    private appData(){}
    public static appData getInstance() {
        return me;
    }

    public static Object getId() {
        return id;
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
