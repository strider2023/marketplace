package com.touchmenotapps.marketplace.bo;

import android.content.Context;

import com.touchmenotapps.marketplace.framework.bo.BaseDao;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by arindamnath on 30/12/17.
 */

public class HoursOfOperationDao extends BaseDao {

    private Map<String, Set<String>> hoursMap = new HashMap<>();
    private List<DailyTimeInfoDao> dailyTimeInfoList = new ArrayList<>();
    private String today;
    private String currentDayStatus = "Closed";

    public HoursOfOperationDao() {
        Date now = new Date();
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E");
        today  = simpleDateformat.format(now);
    }

    public Map<String, Set<String>> getHoursMap() {
        return hoursMap;
    }

    public List<DailyTimeInfoDao> getDailyTimeInfoList() {
        return dailyTimeInfoList;
    }

    public void addHoursMap(String day, String time) {
        Set<String> timeSet = new HashSet<>();
        timeSet.add(time);
        this.hoursMap.put(day, timeSet);
    }

    public String getCurrentDayStatus() {
        return currentDayStatus;
    }

    public void setHoursMap(Map<String, Set<String>> hoursMap) {
        this.hoursMap = hoursMap;
    }

    @Override
    public void parse(JSONParser jsonParser, JSONObject jsonObject) throws Exception {
        for (Object key : jsonObject.keySet()) {
            JSONArray timeArray = (JSONArray) jsonParser.parse(jsonObject.get(key).toString());
            Set<String> timeSet = new HashSet<>();
            timeSet.add(timeArray.get(0).toString());
            hoursMap.put(key.toString(), timeSet);
            if(key.toString().equalsIgnoreCase(today)) {
                currentDayStatus = timeArray.get(0).toString();
            }
            dailyTimeInfoList.add(new DailyTimeInfoDao(today, key.toString(), timeArray.get(0).toString()));
        }
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        //jsonObject.putAll(hoursMap);
        for(String key : hoursMap.keySet()) {
            JSONArray timeArray = new JSONArray();
            for (String time : hoursMap.get(key)) {
                timeArray.add(time);
            }
            //timeArray.add(categoriesMap.get(key));
            jsonObject.put(key, timeArray);
        }
        return jsonObject;
    }
}
