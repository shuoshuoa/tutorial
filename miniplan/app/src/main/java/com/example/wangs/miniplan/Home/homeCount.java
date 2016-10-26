package com.example.wangs.miniplan.Home;

/**
 * Created by wangshuo on 16/4/2.
 */
public class homeCount {
    String homePlan;
    String homeData;

    public homeCount(String homePlan, String homeData) {
        this.homePlan = homePlan;
        this.homeData = homeData;
    }

    public String getHomePlan() {
        return homePlan;
    }

    public void setHomePlan(String homePlan) {
        this.homePlan = homePlan;
    }

    public String getHomeData() {
        return homeData;
    }

    public void setHomeData(String homeData) {
        this.homeData = homeData;
    }

    @Override
    public String toString() {
        return "homeCount{" +
                "homePlan='" + homePlan + '\'' +
                ", homeData='" + homeData + '\'' +
                '}';
    }
}
