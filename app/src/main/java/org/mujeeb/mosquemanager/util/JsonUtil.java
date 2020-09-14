package org.mujeeb.mosquemanager.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Mohammad Mujeeb on 16-12-2017.
 */

public class JsonUtil {

    public static Object objectFromJson(String json, Class targetClass) {

        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, targetClass);
    }

    public static String jsonFromObject(Object object) {

        Gson gson = new GsonBuilder().create();
        return gson.toJson(object);
    }
}
