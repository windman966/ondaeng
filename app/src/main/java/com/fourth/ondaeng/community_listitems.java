package com.fourth.ondaeng;

import java.util.Date;

class community_listitems {
    private String userid;
    private String title;
    private String content;
    private String category;
    private Date date;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public community_listitems(String userid, String title, String content, String category, Date date) {
        this.userid = userid;
        this.title = title;
        this.content = content;
        this.category = category;
        this.date = date;
    }
}
