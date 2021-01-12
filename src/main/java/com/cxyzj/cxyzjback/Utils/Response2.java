package com.cxyzj.cxyzjback.Utils;

import com.google.gson.Gson;

import java.util.HashMap;

/**
 * @Author Yaser
 * @Date 4/25/2019 12:01 PM
 * @Description:
 */
public class Response2 {
    HashMap<String, Object> data = new HashMap<>();

    public void Insert(String key, Object value) {
        data.put(key, value);
    }

    public String sendSuccess() {
        Insert("status", 200);
        return new Gson().toJson(data);
    }

    public String sendFailure() {
        Insert("status", 400);
        return new Gson().toJson(data);
    }
}
