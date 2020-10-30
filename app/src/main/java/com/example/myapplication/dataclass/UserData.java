package com.example.myapplication.dataclass;

import android.graphics.Bitmap;

public class UserData {
    public String email;
    public String nickname;
    public String selfintro;
    //public Bitmap proimage;

    public UserData(String email,String nickname,String selfintro){
        this.email = email;
        this.nickname = nickname;
        this.selfintro = selfintro;
        //this.proimage = proimage;
    }
}
