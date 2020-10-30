package com.example.myapplication.dataclass;

public class ToukouData {
    public String uid;
    public String nickname;
    public String title;
    public String gool;
    public String menu;
    public String memo;
    public String time;

    public ToukouData(String uid,String nickname,String title, String gool, String menu, String memo,String time){
        this.uid = uid;
        this.nickname = nickname;
        this.title = title;
        this.gool = gool;
        this.menu = menu;
        this.memo = memo;
        this.time = time;
    }

    public String getUid(){
        return uid;
    }
    public String getNickname(){
        return nickname;
    }
    public String getTitle(){
        return title;
    }
    public String getGool(){
        return gool;
    }
    public String getMenu(){
        return menu;
    }
    public String getMemo(){
        return memo;
    }
    public String getTime(){
        return time;
    }


}
