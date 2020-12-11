package com.example.Trainees.dataclass;

public class ToukouData {
    public String uid;
    public String title;
    public String goal;
    public String menu;
    public String memo;
    public String time;
    public int comnum;

    public ToukouData(String uid,String title, String goal, String menu, String memo,String time,int comnum){
        this.uid = uid;
        this.title = title;
        this.goal = goal;
        this.menu = menu;
        this.memo = memo;
        this.time = time;
        this.comnum = comnum;
    }

    public ToukouData(){
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getGoal(){
        return goal;
    }
    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getMenu(){
        return menu;
    }
    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getMemo(){
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTime(){
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public int getComnum() { return comnum; }
    public void setComnum(int comnum) { this.comnum = comnum; }
}
