package com.car.vendor.api;

import org.json.JSONObject;

/**
 * Created by AMAN on 25-10-2015.
 */
public class Utils {
    public static boolean contains(JSONObject jsonObject, String key) {
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key) ? true : false;
    }

}
