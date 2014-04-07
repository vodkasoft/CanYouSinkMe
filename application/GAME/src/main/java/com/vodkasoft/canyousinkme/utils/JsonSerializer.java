package com.vodkasoft.canyousinkme.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Vodkasoft (R)
 * Created by jomarin on 4/6/14.
 */
public class JsonSerializer {

    public static Gson gson = new Gson();

    public static String fromObjectToJson(Object pObject){
        return gson.toJson(pObject);
    }

    public static Object fromJsonToObject(String pJson, Type pType){
        return gson.fromJson(pJson, pType);
    }
}
