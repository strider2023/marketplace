package com.touchmenotapps.marketplace.framework.bo;

import android.content.Context;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by i7 on 16-12-2017.
 */

public abstract class BaseDao {

    private Long id;
    private Date today;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat dateOfTheMonth;
    private DecimalFormat amountFormatter;
    private JSONParser jsonParser;

    public BaseDao() {
        this.today = new Date();
        this.dateOfTheMonth = new SimpleDateFormat("dd");
        this.dateFormat = new SimpleDateFormat("E, dd MMM yyyy");
        this.amountFormatter = new DecimalFormat("##,##,###.##");
        this.jsonParser = new JSONParser();
    }

    protected abstract void parse(JSONParser jsonParser, JSONObject jsonObject) throws Exception;

    public DecimalFormat getAmountFormatter() {
        return amountFormatter;
    }

    public String getDate(String dateAsString) throws Exception {
        SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        Date date = serverFormat.parse(dateAsString);
        return dateFormat.format(date);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SimpleDateFormat getDateOfTheMonth() {
        return dateOfTheMonth;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public JSONParser getJsonParser() {
        return jsonParser;
    }
}
