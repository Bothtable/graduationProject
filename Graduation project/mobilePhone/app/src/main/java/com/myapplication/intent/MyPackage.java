package com.myapplication.intent;

import org.json.JSONException;
import org.json.JSONObject;

public class MyPackage {

    public static JSONObject jsonPack(String key1, int value1, String key2, String value2,
                                      String key3, String value3, String key4 , String value4){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key1,value1);
            jsonObject.put(key2,value2);
            jsonObject.put(key3,value3);
            jsonObject.put(key4,value4);
            return jsonObject;
        }catch (JSONException e){
            e.printStackTrace();
        }

        return jsonObject;
    }
    public static JSONObject jsonPack(String key1, int value1, String key2, String value2,
                                      String key3, String value3){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key1,value1);
            jsonObject.put(key2,value2);
            jsonObject.put(key3,value3);
            return jsonObject;
        }catch (JSONException e){
            e.printStackTrace();
        }

        return jsonObject;
    }
}

