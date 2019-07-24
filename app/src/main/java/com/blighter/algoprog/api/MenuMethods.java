package com.blighter.algoprog.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.blighter.algoprog.R;


public class MenuMethods {
    //выход из системы
    public static void menuExit(Context context, android.support.v7.app.ActionBar actionBar) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.app_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
        actionBar.setTitle("Неизвестный Пользователь");
        actionBar.setSubtitle("");
    }
}
