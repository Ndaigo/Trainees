package com.example.Trainees.dataclass;

public class TrainingData {
    public String training;
    public String weight;
    public String count;
    public String times;
    public String memo;

    public TrainingData(String training,String weight,String count,String times,String memo){
        this.training = training;
        this.weight = weight;
        this.count = count;
        this.times = times;
        this.memo = memo;
    }

    public TrainingData(){}

    public String getTraining() { return training; }
    public void setTraining(String training) { this.training = training; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public String getCount() { return count; }
    public void setCount(String count) { this.count = count; }

    public String getTimes() { return times; }
    public void setTimes(String times) { this.times = times; }

    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }
}
