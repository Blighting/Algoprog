package com.blighter.algoprog.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.blighter.algoprog.Fragments.ModuleFragment;
import com.blighter.algoprog.POJO.Cookies;
import com.blighter.algoprog.POJO.UserData;
import com.blighter.algoprog.POJO.me;
import com.blighter.algoprog.POJO.myUser;
import com.blighter.algoprog.R;
import com.blighter.algoprog.RETROFIT.AuthorizationInterface;
import com.blighter.algoprog.RETROFIT.MeInterface;
import com.blighter.algoprog.RETROFIT.MyUserInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.blighter.algoprog.API.MustToUseMethods.setNiceTitle;
import static com.blighter.algoprog.Fragments.LoginFragment.APP_PREFERENCES;


public class ApiMethods {
    public static final String COOKIES = "cookies";
    public static final Boolean WEHAVECOOKIES = false;

    public static void setAboutCourse(final Context context, final FragmentActivity activity) {

        Bundle bundle = new Bundle();
        bundle.putString("id", "0");
        ModuleFragment moduleFragment = new ModuleFragment();
        moduleFragment.setArguments(bundle);
        android.support.v4.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_in_Main, moduleFragment);
        fragmentTransaction.commit();
    }


    // отправление запроса для получения класса MyUser
    public static void askForMyUser(String cookies, final Context context) {

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://algoprog.ru/api/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        MyUserInterface client = retrofit.create(MyUserInterface.class);
        Call<myUser> call = client.getMyUserInfo(cookies);
        call.enqueue(new Callback<myUser>() {
                         @Override
                         public void onResponse(Call<myUser> call, Response<myUser> response) {
                             final myUser userData = response.body();
                         }

                         @Override
                         public void onFailure(Call<myUser> call, Throwable t) {
                             Toast.makeText(context, "succ", Toast.LENGTH_SHORT).show();
                         }
                     }
        );
    }

    // отправление запроса для получения класса Me
    public static void askForMe(String cookies, final Context context) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://algoprog.ru/api/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        MeInterface client = retrofit.create(MeInterface.class);
        Call<me> call = client.getMeInfo(cookies);
        call.enqueue(new Callback<me>() {
            @Override
            public void onResponse(Call<me> call, Response<me> response) {
            }

            @Override
            public void onFailure(Call<me> call, Throwable t) {
                Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // отправление запроса для получения класса Cookies
    public static void sendDataForCookies(UserData user, final Context context, final android.support.v7.app.ActionBar actionBar) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://algoprog.ru/api/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        AuthorizationInterface client = retrofit.create(AuthorizationInterface.class);
        Call<Cookies> call = client.getCookies(user);
        call.enqueue(new Callback<Cookies>() {
            @Override
            public void onResponse(Call<Cookies> call, Response<Cookies> response) {
                if (response.body() != null) {
                    String[] cookiesAndSomething = response.headers().get("Set-Cookie").split(";");
                    SharedPreferences data = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = data.edit();
                    editor.putString(COOKIES, cookiesAndSomething[0]);
                    editor.putBoolean("WEHAVECOOKIES", true);
                    editor.apply();
                    setNiceTitle(actionBar, context);
                } else {
                    Toast.makeText(context, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cookies> call, Throwable t) {
                Toast.makeText(context, "Не удалось связаться с сервером. Проверьте подключение к интернету.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}