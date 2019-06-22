package com.blighter.algoprog.api;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.blighter.algoprog.R;
import com.blighter.algoprog.pojo.Cookies;
import com.blighter.algoprog.pojo.Materials;
import com.blighter.algoprog.pojo.MaterialsInTaskList;
import com.blighter.algoprog.pojo.Task;
import com.blighter.algoprog.pojo.UserData;
import com.blighter.algoprog.pojo.me;
import com.blighter.algoprog.pojo.myUser;
import com.blighter.algoprog.retrofit.AuthorizationInterface;
import com.blighter.algoprog.retrofit.MeInterface;
import com.blighter.algoprog.retrofit.MyUserInterface;
import com.blighter.algoprog.retrofit.TaskInterface;
import com.blighter.algoprog.retrofit.TaskListInterface;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.blighter.algoprog.api.MustToUseMethods.setNiceTitle;
import static com.blighter.algoprog.fragments.LoginFragment.APP_PREFERENCES;


public class ApiMethods {
    public static final String COOKIES = "cookies";
    public static final Boolean WEHAVECOOKIES = false;


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
                             Toast.makeText(context, "Не удалось связаться с сервером. Проверьте подключение к интернету.", Toast.LENGTH_SHORT).show();
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
    public static void sendDataForCookies(UserData user, final Context context, final android.support.v7.app.ActionBar actionBar, final Activity activity) {
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
                    NavigationView navView = (NavigationView) activity.findViewById(R.id.nav_viewInMain);
                    Menu menu = navView.getMenu();
                    menu.add(R.id.settings_and_enter, R.id.nav_enter + 200, 2, R.string.change_user).setIcon(R.drawable.ic_menu_import_export_black);
                    menu.add(R.id.settings_and_enter, R.id.nav_enter + 100, 2, R.string.exit).setIcon(R.drawable.ic_menu_export_black);
                    menu.removeItem(R.id.nav_enter);
                    navView.invalidate();
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

    public static Materials[] getTasksList(String id) {
        Materials[] materials = null;
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://algoprog.ru/api/material/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        TaskListInterface client = retrofit.create(TaskListInterface.class);
        Call<MaterialsInTaskList> call = client.getTasks(id);
        try {
            Response<MaterialsInTaskList> response = call.execute();
            if (response.body() != null) {
                materials = response.body().getMaterials();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return materials;
    }

    public static void setTask(final String id, final WebView browser, final Context context) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://algoprog.ru/api/material/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        TaskInterface client = retrofit.create(TaskInterface.class);
        Call<Task> call = client.getTask(id);
        call.enqueue(new Callback<Task>() {

            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                String styles ="<head> \n" +
                        "  <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css\"> \n" +
                        "  <link rel=\"stylesheet\" href=\"/bundle.css\"> \n" +
                        "  <link rel=\"stylesheet\" href=\"/bootstrap.min.css\"> \n" +
                        "  <link rel=\"stylesheet\" href=\"/react-diff-view.css\"> \n" +
                        "  <link rel=\"stylesheet\" href=\"/informatics.css\"> \n" +
                        "  <link rel=\"stylesheet\" href=\"/highlight.css\"> \n" +
                        "  <link rel=\"stylesheet\" href=\"/main.css\"> \n" +
                        " <meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" />" +
                        "</head>" +
                        "<script type=\"text/x-mathjax-config\">" +
                        "  MathJax.Hub.Config({\n" +
                        "  CommonHTML: { linebreaks: { automatic: true },EqnChunk:(MathJax.Hub.Browser.isMobile?10:50) },displayAlign: \"left\",\n" +
                        "  \"HTML-CSS\": { linebreaks: { automatic: true } ," +
                        "\n" +
                        "    preferredFont: \"STIX\"}," +
                        "extensions: [\"tex2jax.js\"],messageStyle:\"none\"," +
                        "jax: [\"input/TeX\", \"input/MathML\",\"output/HTML-CSS\"]," +
                        "tex2jax: {inlineMath: [['$','$'],['\\\\(','\\\\)']]}" +
                        "});" +
                        "</script>" +
                        "<script type=\"text/javascript\" async src=\"file:///android_asset/MathJax/MathJax.js?config=TeX-AMS-MML_HTMLorMML\"></script>" +
                        "" +
                        "</body>" +
                        "</html>";
                String basicUrl = "https://algoprog.ru/material/" + id;
                String content = response.body().getContent();
                browser.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                browser.getSettings().setJavaScriptEnabled(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    browser.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                }
                browser.loadDataWithBaseURL(basicUrl, styles + content, "text/html", "utf-8", "");
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Toast.makeText(context, "Не удалось связаться с сервером. Проверьте подключение к интернету.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
