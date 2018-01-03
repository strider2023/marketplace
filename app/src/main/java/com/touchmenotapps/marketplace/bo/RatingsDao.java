package com.touchmenotapps.marketplace.bo;

import android.content.Context;

import com.touchmenotapps.marketplace.framework.bo.BaseDao;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by arindamnath on 30/12/17.
 */

public class RatingsDao extends BaseDao {

    public RatingsDao(Context context) {
        super(context);
    }

    @Override
    public void parse(JSONParser jsonParser, JSONObject jsonObject) throws Exception {

    }
}
