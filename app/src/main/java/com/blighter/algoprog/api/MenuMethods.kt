package com.blighter.algoprog.api

import android.content.Context
import androidx.appcompat.app.ActionBar
import com.blighter.algoprog.R

//this class contains methods which are interact with menu and can be used in several fragments
object MenuMethods {
    //this method sets user information as unknown(he got no cookies) and ActionBar
    fun menuExit(context: Context, actionBar: ActionBar?) {
        val sharedPref = context.getSharedPreferences(context.getString(R.string.app_preferences), Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.remove(context.getString(R.string.password))
        editor.remove(context.getString(R.string.user_id))
        editor.remove(context.getString(R.string.login))
        editor.remove(context.getString(R.string.cookies))
        editor.putBoolean(context.getString(R.string.noCookies), true)
        editor.putBoolean(context.getString(R.string.oldCookies), true)
        editor.apply()
        actionBar?.title = "Неизвестный Пользователь"
        actionBar?.subtitle = ""
    }
}