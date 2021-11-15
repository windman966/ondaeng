package com.fourth.ondaeng;

public class postData {
    private String id;
    private String title;
    private String date;
    private int postNo;

    public postData(String id, String title, String date, int postNo){
        this.id = id;
        this.title = title;
        this.date = date;
        this.postNo = postNo;

    }

    public String getid()
    {
        return this.id;
    }

    public String gettitle()
    {
        return this.title;
    }

    public String getdate()
    {
        return this.date;
    }

    public int getpostNo()
    {
        return this.postNo;
    }

}
