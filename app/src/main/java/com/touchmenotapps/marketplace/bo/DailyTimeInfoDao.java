package com.touchmenotapps.marketplace.bo;

/**
 * Created by i7 on 22-01-2018.
 */

public class DailyTimeInfoDao {

    private String day;
    private String time;

    public DailyTimeInfoDao(String today, String day, String time) {
        if(day.equalsIgnoreCase(today)) {
            this.day = "<b>" + day + "</b>";
            this.time = "<b>" + time + "</b>";
        } else {
            this.day = day;
            this.time = time;
        }
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
