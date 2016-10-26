package com.example.wangs.miniplan.AllPlans;

/**
 * Created by renjie on 2016/1/20.
 */
public class plans {
    int id;
    String planTitle;
    String planTime;
    String alarmTime;
    String alarm;
    String isDone;

    public plans(int id, String planTitle, String planTime, String alarmTime, String alarm, String isDone) {
        this.id = id;
        this.planTitle = planTitle;
        this.planTime = planTime;
        this.alarmTime = alarmTime;
        this.alarm = alarm;
        this.isDone = isDone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public String getPlanTime() {
        return planTime;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }


    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getIsDone() {
        return isDone;
    }

    public void setSiDone(String isDone) {
        this.isDone = isDone;
    }

    @Override
    public String toString() {
        return "plans{" +
                "id=" + id +
                ", planTitle='" + planTitle + '\'' +
                ", planTime='" + planTime + '\'' +
                ", alarmTime='" + alarmTime + '\'' +
                ", alarm='" + alarm + '\'' +
                ", siDone='" + isDone + '\'' +
                '}';
    }
}
