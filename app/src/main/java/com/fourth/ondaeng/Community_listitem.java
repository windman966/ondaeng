package com.fourth.ondaeng;

import java.util.Date;

public class Community_listitem {
    private String nickname;
    private String title;
    private Date date;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public Community_listitem(String nickname, String title, Date date) {
        this.nickname = nickname;
        this.title = title;
        this.date = date;
    }
}
