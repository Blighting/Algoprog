package com.blighter.algoprog.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.Toast;

import com.blighter.algoprog.pojo.myUser;
import com.blighter.algoprog.retrofit.MyUserInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.blighter.algoprog.api.ApiMethods.COOKIES;
import static com.blighter.algoprog.fragments.LoginFragment.APP_PREFERENCES;

public class MustToUseMethods {
    private static float[] getHSV(int rating, double activity) {
        double R = 160000.0;
        double A = 7.0;
        double h = 11.0 / 12.0 * (1.0 - Math.log(rating + 1.0) / Math.log(R + 1.0));
        double v = 0.3 + 0.7 * Math.log(activity + 1.0) / Math.log(A + 1.0);
        h *= 366;
        float h1 = (float) h;
        float v1 = (float) v;
        return new float[]{h1, 100, v1};

    }

    private static void askForMyUser(final String cookies, final Context context, final android.support.v7.app.ActionBar actionBar) {
        actionBar.setTitle("");
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://algoprog.ru/api/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        MyUserInterface client = retrofit.create(MyUserInterface.class);
        Call<myUser> call = client.getMyUserInfo(cookies);
        call.enqueue(new Callback<myUser>() {
                         @Override
                         public void onResponse(Call<myUser> call, Response<myUser> response) {
                             if (response.body() != null) {
                                 SpannableStringBuilder coolSubtitle = new SpannableStringBuilder();
                                 myUser user = response.body();
                                 String name = user.getName();
                                 String rating = user.getRating().toString();
                                 double fullActivity = user.getActivity();
                                 int firstPartOfActivity = (int) fullActivity;
                                 int secondPartOfActivity = (int) ((fullActivity * 10) % 10);
                                 String activity = firstPartOfActivity + "." + secondPartOfActivity;
                                 SpannableString coolSubtitlePartOne = new SpannableString(rating);
                                 SpannableString coolSubtitlePartTwo = new SpannableString("/" + activity);
                                 coolSubtitlePartOne.setSpan(new ForegroundColorSpan(Color.HSVToColor(getHSV(user.getRating(), user.getActivity()))), 0, rating.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                 coolSubtitle.append(coolSubtitlePartOne);
                                 coolSubtitlePartTwo.setSpan(new ForegroundColorSpan(Color.BLACK), 0, activity.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                 coolSubtitle.append(coolSubtitlePartTwo);
                                 SpannableString coolTitle = new SpannableString(user.getName() + "  " + user.getLevel().getCurrent());
                                 coolTitle.setSpan(new ForegroundColorSpan(Color.HSVToColor(getHSV(user.getRating(), user.getActivity()))), 0, name.length(), 0);
                                 actionBar.setTitle(coolTitle);
                                 actionBar.setSubtitle(coolSubtitle);
                             } else {
                                 actionBar.setTitle("Неизвестный пользователь");
                                 Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                             }
                         }

                         @Override
                         public void onFailure(Call<myUser> call, Throwable t) {
                             Toast.makeText(context, "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
                         }
                     }
        );
    }

    public static void setNiceTitle(android.support.v7.app.ActionBar actionBar, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean authorized = sharedPref.getBoolean("WEHAVECOOKIES", false);
        if (authorized) {
            askForMyUser(sharedPref.getString(COOKIES, ""), context, actionBar);
        } else
            actionBar.setTitle("Неизвестный пользователь");
    }
}
