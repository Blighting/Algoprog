package com.blighter.algoprog.api

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.room.Room
import com.blighter.algoprog.R
import com.blighter.algoprog.data.Database
import com.blighter.algoprog.network.*
import com.blighter.algoprog.network.ClientCoroutines.client
import com.blighter.algoprog.utils.RoundedBackgroundSpan
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.*
import retrofit2.Response
import kotlin.math.ln

//this method using a formula that Kalinin created to generate color for user name
fun getHSV(rating: Int, activity: Double): FloatArray? {
    val r = 160000.0
    val a = 7.0
    var h = 11.0 / 12.0 * (1.0 - ln(rating + 1.0) / ln(r + 1.0))
    var v = 0.3 + 0.7 * ln(activity + 1.0) / ln(a + 1.0)
    if (0.1 > activity) {
        v = 0.0
    }
    h *= 366.0
    val h1 = h.toFloat()
    val v1 = v.toFloat()
    return floatArrayOf(h1, 100f, v1)
}

sealed class Errors(val warn: String)
class Unknown : Errors("Неизвестная ошибка")
class NoInternet : Errors("Нет подключения к интернету")
class WrongLogPass : Errors("Неверный логин или пароль")


data class MyUserInfoResponse(val error: String?, val title: SpannableString?, val subtitle: SpannableStringBuilder?, val myUser: MyUser?)

suspend fun getMyUserInfoAndCookies(userData: UserData?, context: Context): Deferred<MyUserInfoResponse?> = coroutineScope {
    async {
        var myUserInfoResponse: MyUserInfoResponse? = null
        var cookiesResponse: Response<Cookies?>? = null
        var error: String? = null
        try {
            cookiesResponse = client.create(AuthorizationInterface::class.java).getCookies(userData)?.execute()
        } catch (e: Exception) {
            error = NoInternet().warn
        }
        if (error != null) {
            myUserInfoResponse = MyUserInfoResponse(error, null, null, null)
        } else if (!cookiesResponse!!.isSuccessful) {
            myUserInfoResponse = MyUserInfoResponse(WrongLogPass().warn, null, null, null)
        } else {
            val cookies = cookiesResponse.headers().toString()
                    .substringAfter("set-cookie:")
                    .takeWhile { it != ';' }
//saving it in sharPref
            val sharedPreferences = context.getSharedPreferences("justSomeData", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(context.getString(R.string.cookies), cookies).apply()
            myUserInfoResponse = getMyUserInfo(cookies, context).await()
//if we dont have userId we insert it
            if (!sharedPreferences.contains(context.getString(R.string.user_id))) {
                editor.putString(context.getString(R.string.user_id), myUserInfoResponse?.myUser?._id).apply()
            }
            editor.putString(context.getString(R.string.login), userData?.username)
            editor.putString(context.getString(R.string.password), userData?.password)
            editor.putBoolean(context.getString(R.string.oldCookies), false)
            editor.putBoolean(context.getString(R.string.noCookies), false)
            editor.apply()
        }
        myUserInfoResponse
    }
}

suspend fun getMyUserInfo(cookies: String?, context: Context): Deferred<MyUserInfoResponse?> = coroutineScope {
    async {
        var myUser: MyUser? = null
        var error: Boolean? = null
        var myUserResponse: MyUserInfoResponse? = null
        try {
            myUser = client.create(MyUserInterface::class.java).getMyUserInfo(cookies)?.execute()?.body()
        } catch (e: Exception) {
            error = true
        }

        when {
            error == true -> {
                myUserResponse = MyUserInfoResponse(NoInternet().warn, null, null, null)
            }
            myUser == null -> {
                myUserResponse = MyUserInfoResponse(Unknown().warn, null, null, null)
            }
            else -> {
                val coolSubtitle = SpannableStringBuilder()
                val firstPartOfActivity = myUser.activity.toInt()
                val secondPartOfActivity = (myUser.activity * 10 % 10).toInt()
                val activity = "$firstPartOfActivity.$secondPartOfActivity"
                val rating = myUser.rating.toString()
                val coolSubtitlePartOne =

                        SpannableString(rating)
                val coolSubtitlePartTwo = SpannableString("/$activity")
                coolSubtitlePartOne.setSpan(ForegroundColorSpan
                (Color.HSVToColor(
                        getHSV(myUser.rating, myUser.activity))), 0, rating.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                coolSubtitle.append(coolSubtitlePartOne)
                coolSubtitlePartTwo.setSpan(ForegroundColorSpan(Color.BLACK), 0, activity.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                coolSubtitle.append(coolSubtitlePartTwo)
                coolSubtitle.append(" ")
                val coolTitle = SpannableString(myUser.name + " " + myUser.level.current)
                coolTitle.setSpan(ForegroundColorSpan(
                        Color.HSVToColor(
                                getHSV(myUser.rating, myUser.activity))), 0, myUser.name.length, 0)
                val db = Room.databaseBuilder(context, Database::class.java, "achievements.db")
                        .fallbackToDestructiveMigration()
                        .createFromAsset("databases/achievements.db")
                        .build()
                val dao = db.getDao()
                for (i in 0..2) {
                    val achievement = dao.getById(myUser.achieves[i])
                    val text = SpannableString(achievement!!.text)
                    text.setSpan(RoundedBackgroundSpan(Color.parseColor(achievement.color)), 0, text.length, 0)
                    coolSubtitle.append(text)
                    coolSubtitle.append(" ")
                }
                myUserResponse = MyUserInfoResponse(null, coolTitle, coolSubtitle, myUser)
            }
        }
        myUserResponse
    }
}

fun setNiceTitle(actionBar: ActionBar?, context: Context, navView: NavigationView, firstTimeLoggingIn: Boolean, userData: UserData?) {
    val sharedPref = context.getSharedPreferences(context.getString(R.string.app_preferences), Context.MODE_PRIVATE)
    val noCookies = sharedPref.getBoolean(context.getString(R.string.noCookies), true)
    val oldCookies = sharedPref.getBoolean(context.getString(R.string.oldCookies), true)
    var myUserInfoResponse: MyUserInfoResponse? = null
    if (!noCookies) {
        scopeNetwork.launch {
            if (!oldCookies) {
                myUserInfoResponse = getMyUserInfo(sharedPref.getString(context.getString(R.string.cookies), ""), context).await()
            } else {
                val editor = sharedPref.edit()
                //TODO maybe we should say that cookies aren't old if we sure that we got them and they are good
                editor.putBoolean(context.getString(R.string.oldCookies), false).apply()
                if (userData == null) {
                    if (sharedPref.getString(context.getString(R.string.login), "") != "") {
                        val userDataFromPref = UserData(sharedPref.getString(context.getString(R.string.login), ""), sharedPref.getString(context.getString(R.string.password), ""))
                        myUserInfoResponse = getMyUserInfoAndCookies(userDataFromPref, context).await()
                    }
                } else {
                    myUserInfoResponse = getMyUserInfoAndCookies(userData, context).await()
                }
            }
            withContext(Dispatchers.Main) {
                if (myUserInfoResponse != null && myUserInfoResponse?.error == null) {
                    actionBar?.title = myUserInfoResponse!!.title
                    actionBar?.subtitle = myUserInfoResponse!!.subtitle
                    val menu = navView.menu
                    menu.clear()
                    navView.inflateMenu(R.menu.drawer_logged)
                    navView.invalidate()
                    if (firstTimeLoggingIn) {
                        (context as Activity).onBackPressed()
                    }
                } else {
                    if (myUserInfoResponse?.error != null) {
                        Toast.makeText(context, myUserInfoResponse?.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    } else {
        actionBar?.setTitle("Неизвестный пользователь")
    }
}