package com.example.Trainees.dataclass;

public class CommentData {
    public String uid;
    public String comment;
    public String time;

    public CommentData(String uid,String comment,String time){
        this.uid = uid;
        this.comment = comment;
        this.time = time;
    }

    public CommentData(){
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getComment(){return comment;}
    public void setComment(String comment) { this.comment = comment; }

    public String getTime(){
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
