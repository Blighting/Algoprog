package com.blighter.algoprog.API;

import android.content.Context;
import android.content.SharedPreferences;

import static com.blighter.algoprog.Fragments.LoginFragment.APP_PREFERENCES;


public class MenuMethods {
    //выход из системы
    public static void menuExit(Context context, android.support.v7.app.ActionBar actionBar) {
        SharedPreferences sharedPref = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
        actionBar.setTitle("Неизвестный Пользователь");
        actionBar.setSubtitle("");
    }
}
