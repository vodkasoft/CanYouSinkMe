package com.vodkasoft.canyousinkme.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Vodkasoft (R)
 * Created by jomarin on 4/6/14.
 */
public class JsonSerializer {

    /**
     * Creates Gson member instance
     */
    public static Gson gson = new Gson();

    /**
     * Converts any object type to json. Note: primitives are recommended
     * @param pObject to be converted
     * @return
     */
    public static String fromObjectToJson(Object pObject){
        return gson.toJson(pObject);
    }

    /**
     * Converts any json to object type, needs to be casted
     * @param pJson
     * @param pType
     * @return
     */
    public static Object fromJsonToObject(String pJson, Type pType){
        return gson.fromJson(pJson, pType);
    }
}
