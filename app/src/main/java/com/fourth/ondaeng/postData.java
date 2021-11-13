package com.fourth.ondaeng;

public class postData {
    private String id;
    private String title;
    private String date;

    public postData(String id, String title, String date){
        this.id = id;
        this.title = title;
        this.date = date;
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

}
