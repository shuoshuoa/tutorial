package com.example.wangs.miniplan.CountPlan;

/**
 * Created by wangshuo on 16/2/28.
 */
public class planeds {
    int id;
    String planedName;  //计划名
    String planTime;       //计划时长
    int planedTime;     //执行时长
    String planedDate;  //执行计划日期
    String planingTime; //执行计划时间
    String planedMood;  //执行计划感想
    int planedPercentage; //计划完成率

    public planeds(int id, String planedName, String planTime, int planedTime, String planedDate, String planingTime, String planedMood, int planedPercentage) {
        this.id = id;
        this.planedName = planedName;
        this.planTime = planTime;
        this.planedTime = planedTime;
        this.planedDate = planedDate;
        this.planingTime = planingTime;
        this.planedMood = planedMood;
        this.planedPercentage = planedPercentage;
    }

    @Override
    public String toString() {
        return "planeds{" +
                "id=" + id +
                ", planedName='" + planedName + '\'' +
                ", planTime=" + planTime +
                ", planedTime=" + planedTime +
                ", planedDate='" + planedDate + '\'' +
                ", planingTime='" + planingTime + '\'' +
                ", planedMood='" + planedMood + '\'' +
                ", planedPercentage=" + planedPercentage +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlanedName() {
        return planedName;
    }

    public void setPlanedName(String planedName) {
        this.planedName = planedName;
    }

    public String getPlanTime() {
        return planTime;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    public int getPlanedTime() {
        return planedTime;
    }

    public void setPlanedTime(int planedTime) {
        this.planedTime = planedTime;
    }

    public String getPlanedDate() {
        return planedDate;
    }

    public void setPlanedDate(String planedDate) {
        this.planedDate = planedDate;
    }

    public String getPlaningTime() {
        return planingTime;
    }

    public void setPlaningTime(String planingTime) {
        this.planingTime = planingTime;
    }

    public String getPlanedMood() {
        return planedMood;
    }

    public void setPlanedMood(String planedMood) {
        this.planedMood = planedMood;
    }

    public int getPlanedPercentage() {
        return planedPercentage;
    }

    public void setPlanedPercentage(int planedPercentage) {
        this.planedPercentage = planedPercentage;
    }
}

