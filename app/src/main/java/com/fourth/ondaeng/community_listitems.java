package com.fourth.ondaeng;

import java.util.Date;

class community_listitems {
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

    public community_listitems(String nickname, String title, Date date) {
        this.nickname = nickname;
        this.title = title;
        this.date = date;
    }
}
