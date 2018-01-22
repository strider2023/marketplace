package com.touchmenotapps.marketplace.bo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by i7 on 22-01-2018.
 */

public class DailyTimeInfoDao {

    private String day;
    private String time;
    private String today;

    public DailyTimeInfoDao() {
        Date now = new Date();
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated
        today  = simpleDateformat.format(now);
    }

    public DailyTimeInfoDao(String day, String time) {
        Date now = new Date();
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated
        today  = simpleDateformat.format(now);
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
