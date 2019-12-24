package com.hjq.demo.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hjq.demo.common.Constants;

public class SharedPreferenceUtil {
    public static void saveSharedPreference(Context context, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("PMaS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStringFromSharedPreference(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("PMaS", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static void logout(Context context) {
        saveSharedPreference(context, "uid", "");
    }
}
