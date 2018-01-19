package com.touchmenotapps.marketplace.bo;

import android.content.Context;

import com.touchmenotapps.marketplace.framework.bo.BaseDao;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by arindamnath on 30/12/17.
 */

public class HoursOfOperationDao extends BaseDao {

    private Map<String, Set<String>> hoursMap = new HashMap<>();

    public HoursOfOperationDao() {
        /*hoursMap.put("MON", "10AM-10PM");
        hoursMap.put("TUE", "10AM-10PM");
        hoursMap.put("WED", "10AM-10PM");
        hoursMap.put("THUR", "10AM-10PM");
        hoursMap.put("FRI", "10AM-10PM");
        hoursMap.put("SAT", "10AM-10PM");
        hoursMap.put("SUN", "10AM-10PM");*/
    }

    public Map<String, Set<String>> getHoursMap() {
        return hoursMap;
    }

    public void addHoursMap(String day, String time) {
        Set<String> timeSet = new HashSet<>();
        timeSet.add(time);
        this.hoursMap.put(day, timeSet);
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
