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
                String styles = "<head> \n" +
                        "  <meta charset=\"UTF-8\"> \n" +
                        "  <meta name=\"yandex-verification\" content=\"4f0059cd93dfb218\">\n" +
                        "  <title>Наибольшая возрастающая подпоследовательность с восстановлением ответа — algoprog.ru</title>\n" +
                        "  <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css\"> \n" +
                        "  <link rel=\"stylesheet\" href=\"/bundle.css\"> \n" +
                        "  <link rel=\"stylesheet\" href=\"/bootstrap.min.css\"> \n" +
                        "  <link rel=\"stylesheet\" href=\"/react-diff-view.css\"> \n" +
                        "  <link rel=\"stylesheet\" href=\"/informatics.css\"> \n" +
                        "  <link rel=\"stylesheet\" href=\"/highlight.css\"> \n" +
                        "  <link rel=\"stylesheet\" href=\"/main.css\"> \n" +
                        "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> \n" +
                        "  <script type=\"text/javascript\" async=\"\" src=\"https://mc.yandex.ru/metrika/watch.js\"></script><script> window.__PRELOADED_STATE__ = {\"data\":[{\"pending\":true,\"url\":\"myUser\"},{\"pending\":true,\"url\":\"lastComments\"},{\"pending\":true,\"url\":\"lastBlogPosts\"},{\"pending\":true,\"url\":\"material/news\"},{\"pending\":true,\"url\":\"me\"},{\"pending\":true,\"url\":\"material/tree\"},{\"data\":{\"materials\":[],\"force\":false,\"path\":[{\"_id\":\"main\",\"title\":\"/\"},{\"_id\":\"4\",\"title\":\"Уровень 4\"},{\"_id\":\"4А\",\"title\":\"Уровень 4А\"},{\"_id\":\"16578\",\"title\":\"4А: ДП-баяны\"}],\"_id\":\"p1792\",\"order\":1,\"type\":\"problem\",\"title\":\"Наибольшая возрастающая подпоследовательность с восстановлением ответа\",\"content\":\"<h1>Наибольшая возрастающая подпоследовательность с восстановлением ответа</h1><div>\\n   <div class=\\\"legend\\\">\\n    <p>\\n\\t\\tДана последовательность, требуется найти её наибольшую возрастающую подпоследовательность.\\n    </p>\\n   </div>\\n\\n   <div class=\\\"input-specification\\\">\\n    <div class=\\\"section-title\\\">\\n     Входные данные\\n    </div>\\n    <p>\\n\\t\\tВ первой строке входных данных задано число $N$ - длина последовательности (1 ≤ $N$ ≤ 1000). Во второй строке задается сама последовательность (разделитель -  пробел). Элементы последовательности - целые числа, не превосходящие 10000 по модулю.\\n    </p>\\n   </div>\\n\\n   <div class=\\\"output-specification\\\">\\n    <div class=\\\"section-title\\\">\\n     Выходные данные\\n    </div>\\n    <p>\\n\\t\\tТребуется вывести наибольшую возрастающую подпоследовательность данной последовательности. Если таких подпоследовательностей несколько, необходимо вывести одну (любую) из них.\\n    </p>\\n\\n   </div>\\n </div><div><div class=\\\"sample-tests\\\"><div class=\\\"section-title\\\">Примеры</div><div class=\\\"sample-test\\\"><div class=\\\"input\\\"><div class=\\\"title\\\">Входные данные</div><pre class=\\\"content\\\">6\\n3 29 5 5 28 6</pre></div><div class=\\\"output\\\"><div class=\\\"title\\\">Выходные данные</div><pre class=\\\"content\\\">3 5 28</pre></div></div></div></div>\"},\"success\":true,\"url\":\"material/p1792\",\"pending\":true}],\"unknownWarningShown\":false,\"unpaidWarningShown\":false,\"notifications\":[]}; </script> \n" +
                        "  <script type=\"text/x-mathjax-config;executed=true\"> MathJax.Hub.Config({ extensions: [\"tex2jax.js\"], jax: [\"input/TeX\", \"output/HTML-CSS\"], tex2jax: { inlineMath: [ [\"$\",\"$\"] ], displayMath: [ [\"$$\",\"$$\"] ], processEscapes: true }, \"HTML-CSS\": { availableFonts: [\"TeX\"] } }); </script> \n" +
                        "  <script type=\"text/javascript\" src=\"https://cdn.rawgit.com/davidjbradshaw/iframe-resizer/master/js/iframeResizer.contentWindow.min.js\"></script> \n" +
                        "  <script src=\"https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-MML-AM_CHTML\"></script> \n" +
                        "  <script src=\"/highlight.pack.js\"></script> \n" +
                        " <style type=\"text/css\">.MathJax_Hover_Frame {border-radius: .25em; -webkit-border-radius: .25em; -moz-border-radius: .25em; -khtml-border-radius: .25em; box-shadow: 0px 0px 15px #83A; -webkit-box-shadow: 0px 0px 15px #83A; -moz-box-shadow: 0px 0px 15px #83A; -khtml-box-shadow: 0px 0px 15px #83A; border: 1px solid #A6D ! important; display: inline-block; position: absolute}\n" +
                        ".MathJax_Menu_Button .MathJax_Hover_Arrow {position: absolute; cursor: pointer; display: inline-block; border: 2px solid #AAA; border-radius: 4px; -webkit-border-radius: 4px; -moz-border-radius: 4px; -khtml-border-radius: 4px; font-family: 'Courier New',Courier; font-size: 9px; color: #F0F0F0}\n" +
                        ".MathJax_Menu_Button .MathJax_Hover_Arrow span {display: block; background-color: #AAA; border: 1px solid; border-radius: 3px; line-height: 0; padding: 4px}\n" +
                        ".MathJax_Hover_Arrow:hover {color: white!important; border: 2px solid #CCC!important}\n" +
                        ".MathJax_Hover_Arrow:hover span {background-color: #CCC!important}\n" +
                        "</style><style type=\"text/css\">#MathJax_About {position: fixed; left: 50%; width: auto; text-align: center; border: 3px outset; padding: 1em 2em; background-color: #DDDDDD; color: black; cursor: default; font-family: message-box; font-size: 120%; font-style: normal; text-indent: 0; text-transform: none; line-height: normal; letter-spacing: normal; word-spacing: normal; word-wrap: normal; white-space: nowrap; float: none; z-index: 201; border-radius: 15px; -webkit-border-radius: 15px; -moz-border-radius: 15px; -khtml-border-radius: 15px; box-shadow: 0px 10px 20px #808080; -webkit-box-shadow: 0px 10px 20px #808080; -moz-box-shadow: 0px 10px 20px #808080; -khtml-box-shadow: 0px 10px 20px #808080; filter: progid:DXImageTransform.Microsoft.dropshadow(OffX=2, OffY=2, Color='gray', Positive='true')}\n" +
                        "#MathJax_About.MathJax_MousePost {outline: none}\n" +
                        ".MathJax_Menu {position: absolute; background-color: white; color: black; width: auto; padding: 5px 0px; border: 1px solid #CCCCCC; margin: 0; cursor: default; font: menu; text-align: left; text-indent: 0; text-transform: none; line-height: normal; letter-spacing: normal; word-spacing: normal; word-wrap: normal; white-space: nowrap; float: none; z-index: 201; border-radius: 5px; -webkit-border-radius: 5px; -moz-border-radius: 5px; -khtml-border-radius: 5px; box-shadow: 0px 10px 20px #808080; -webkit-box-shadow: 0px 10px 20px #808080; -moz-box-shadow: 0px 10px 20px #808080; -khtml-box-shadow: 0px 10px 20px #808080; filter: progid:DXImageTransform.Microsoft.dropshadow(OffX=2, OffY=2, Color='gray', Positive='true')}\n" +
                        ".MathJax_MenuItem {padding: 1px 2em; background: transparent}\n" +
                        ".MathJax_MenuArrow {position: absolute; right: .5em; padding-top: .25em; color: #666666; font-size: .75em}\n" +
                        ".MathJax_MenuActive .MathJax_MenuArrow {color: white}\n" +
                        ".MathJax_MenuArrow.RTL {left: .5em; right: auto}\n" +
                        ".MathJax_MenuCheck {position: absolute; left: .7em}\n" +
                        ".MathJax_MenuCheck.RTL {right: .7em; left: auto}\n" +
                        ".MathJax_MenuRadioCheck {position: absolute; left: .7em}\n" +
                        ".MathJax_MenuRadioCheck.RTL {right: .7em; left: auto}\n" +
                        ".MathJax_MenuLabel {padding: 1px 2em 3px 1.33em; font-style: italic}\n" +
                        ".MathJax_MenuRule {border-top: 1px solid #DDDDDD; margin: 4px 3px}\n" +
                        ".MathJax_MenuDisabled {color: GrayText}\n" +
                        ".MathJax_MenuActive {background-color: #606872; color: white}\n" +
                        ".MathJax_MenuDisabled:focus, .MathJax_MenuLabel:focus {background-color: #E8E8E8}\n" +
                        ".MathJax_ContextMenu:focus {outline: none}\n" +
                        ".MathJax_ContextMenu .MathJax_MenuItem:focus {outline: none}\n" +
                        "#MathJax_AboutClose {top: .2em; right: .2em}\n" +
                        ".MathJax_Menu .MathJax_MenuClose {top: -10px; left: -10px}\n" +
                        ".MathJax_MenuClose {position: absolute; cursor: pointer; display: inline-block; border: 2px solid #AAA; border-radius: 18px; -webkit-border-radius: 18px; -moz-border-radius: 18px; -khtml-border-radius: 18px; font-family: 'Courier New',Courier; font-size: 24px; color: #F0F0F0}\n" +
                        ".MathJax_MenuClose span {display: block; background-color: #AAA; border: 1.5px solid; border-radius: 18px; -webkit-border-radius: 18px; -moz-border-radius: 18px; -khtml-border-radius: 18px; line-height: 0; padding: 8px 0 6px}\n" +
                        ".MathJax_MenuClose:hover {color: white!important; border: 2px solid #CCC!important}\n" +
                        ".MathJax_MenuClose:hover span {background-color: #CCC!important}\n" +
                        ".MathJax_MenuClose:hover:focus {outline: none}\n" +
                        "</style><style type=\"text/css\">.MathJax_Preview .MJXf-math {color: inherit!important}\n" +
                        "</style><style type=\"text/css\">.MJX_Assistive_MathML {position: absolute!important; top: 0; left: 0; clip: rect(1px, 1px, 1px, 1px); padding: 1px 0 0 0!important; border: 0!important; height: 1px!important; width: 1px!important; overflow: hidden!important; display: block!important; -webkit-touch-callout: none; -webkit-user-select: none; -khtml-user-select: none; -moz-user-select: none; -ms-user-select: none; user-select: none}\n" +
                        ".MJX_Assistive_MathML.MJX_Assistive_MathML_Block {width: 100%!important}\n" +
                        "</style><style type=\"text/css\">#MathJax_Zoom {position: absolute; background-color: #F0F0F0; overflow: auto; display: block; z-index: 301; padding: .5em; border: 1px solid black; margin: 0; font-weight: normal; font-style: normal; text-align: left; text-indent: 0; text-transform: none; line-height: normal; letter-spacing: normal; word-spacing: normal; word-wrap: normal; white-space: nowrap; float: none; -webkit-box-sizing: content-box; -moz-box-sizing: content-box; box-sizing: content-box; box-shadow: 5px 5px 15px #AAAAAA; -webkit-box-shadow: 5px 5px 15px #AAAAAA; -moz-box-shadow: 5px 5px 15px #AAAAAA; -khtml-box-shadow: 5px 5px 15px #AAAAAA; filter: progid:DXImageTransform.Microsoft.dropshadow(OffX=2, OffY=2, Color='gray', Positive='true')}\n" +
                        "#MathJax_ZoomOverlay {position: absolute; left: 0; top: 0; z-index: 300; display: inline-block; width: 100%; height: 100%; border: 0; padding: 0; margin: 0; background-color: white; opacity: 0; filter: alpha(opacity=0)}\n" +
                        "#MathJax_ZoomFrame {position: relative; display: inline-block; height: 0; width: 0}\n" +
                        "#MathJax_ZoomEventTrap {position: absolute; left: 0; top: 0; z-index: 302; display: inline-block; border: 0; padding: 0; margin: 0; background-color: white; opacity: 0; filter: alpha(opacity=0)}\n" +
                        "</style><style type=\"text/css\">.MathJax_Preview {color: #888}\n" +
                        "#MathJax_Message {position: fixed; left: 1px; bottom: 2px; background-color: #E6E6E6; border: 1px solid #959595; margin: 0px; padding: 2px 8px; z-index: 102; color: black; font-size: 80%; width: auto; white-space: nowrap}\n" +
                        "#MathJax_MSIE_Frame {position: absolute; top: 0; left: 0; width: 0px; z-index: 101; border: 0px; margin: 0px; padding: 0px}\n" +
                        ".MathJax_Error {color: #CC0000; font-style: italic}\n" +
                        "</style><style type=\"text/css\" data-styled-components=\"fqkyPI iVfUMe cyQQwJ iXASsm iECmZH bFMHxK\" data-styled-components-is-local=\"true\">\n" +
                        "/* sc-component-id: sc-keyframes-fqkyPI */\n" +
                        "@-webkit-keyframes fqkyPI{0%,80%,100%{box-shadow: 0 0;height: 4em;}40%{box-shadow: 0 -2em;height: 5em;}}@keyframes fqkyPI{0%,80%,100%{box-shadow: 0 0;height: 4em;}40%{box-shadow: 0 -2em;height: 5em;}}\n" +
                        "/* sc-component-id: sc-keyframes-iVfUMe */\n" +
                        "@-webkit-keyframes iVfUMe{0%,80%,100%{box-shadow: 0 2.5em 0 -1.3em;}40%{box-shadow: 0 2.5em 0 0;}}@keyframes iVfUMe{0%,80%,100%{box-shadow: 0 2.5em 0 -1.3em;}40%{box-shadow: 0 2.5em 0 0;}}\n" +
                        "/* sc-component-id: sc-keyframes-cyQQwJ */\n" +
                        "@-webkit-keyframes cyQQwJ{0%,100%{box-shadow: 0 -3em 0 0.2em, 2em -2em 0 0em, 3em 0 0 -1em, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 -1em, -3em 0 0 -1em, -2em -2em 0 0;}12.5%{box-shadow: 0 -3em 0 0, 2em -2em 0 0.2em, 3em 0 0 0, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 -1em, -3em 0 0 -1em, -2em -2em 0 -1em;}25%{box-shadow: 0 -3em 0 -0.5em, 2em -2em 0 0, 3em 0 0 0.2em, 2em 2em 0 0, 0 3em 0 -1em, -2em 2em 0 -1em, -3em 0 0 -1em, -2em -2em 0 -1em;}37.5%{box-shadow: 0 -3em 0 -1em, 2em -2em 0 -1em, 3em 0em 0 0, 2em 2em 0 0.2em, 0 3em 0 0em, -2em 2em 0 -1em, -3em 0em 0 -1em, -2em -2em 0 -1em;}50%{box-shadow: 0 -3em 0 -1em, 2em -2em 0 -1em, 3em 0 0 -1em, 2em 2em 0 0em, 0 3em 0 0.2em, -2em 2em 0 0, -3em 0em 0 -1em, -2em -2em 0 -1em;}62.5%{box-shadow: 0 -3em 0 -1em, 2em -2em 0 -1em, 3em 0 0 -1em, 2em 2em 0 -1em, 0 3em 0 0, -2em 2em 0 0.2em, -3em 0 0 0, -2em -2em 0 -1em;}75%{box-shadow: 0em -3em 0 -1em, 2em -2em 0 -1em, 3em 0em 0 -1em, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 0, -3em 0em 0 0.2em, -2em -2em 0 0;}87.5%{box-shadow: 0em -3em 0 0, 2em -2em 0 -1em, 3em 0 0 -1em, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 0, -3em 0em 0 0, -2em -2em 0 0.2em;}}@keyframes cyQQwJ{0%,100%{box-shadow: 0 -3em 0 0.2em, 2em -2em 0 0em, 3em 0 0 -1em, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 -1em, -3em 0 0 -1em, -2em -2em 0 0;}12.5%{box-shadow: 0 -3em 0 0, 2em -2em 0 0.2em, 3em 0 0 0, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 -1em, -3em 0 0 -1em, -2em -2em 0 -1em;}25%{box-shadow: 0 -3em 0 -0.5em, 2em -2em 0 0, 3em 0 0 0.2em, 2em 2em 0 0, 0 3em 0 -1em, -2em 2em 0 -1em, -3em 0 0 -1em, -2em -2em 0 -1em;}37.5%{box-shadow: 0 -3em 0 -1em, 2em -2em 0 -1em, 3em 0em 0 0, 2em 2em 0 0.2em, 0 3em 0 0em, -2em 2em 0 -1em, -3em 0em 0 -1em, -2em -2em 0 -1em;}50%{box-shadow: 0 -3em 0 -1em, 2em -2em 0 -1em, 3em 0 0 -1em, 2em 2em 0 0em, 0 3em 0 0.2em, -2em 2em 0 0, -3em 0em 0 -1em, -2em -2em 0 -1em;}62.5%{box-shadow: 0 -3em 0 -1em, 2em -2em 0 -1em, 3em 0 0 -1em, 2em 2em 0 -1em, 0 3em 0 0, -2em 2em 0 0.2em, -3em 0 0 0, -2em -2em 0 -1em;}75%{box-shadow: 0em -3em 0 -1em, 2em -2em 0 -1em, 3em 0em 0 -1em, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 0, -3em 0em 0 0.2em, -2em -2em 0 0;}87.5%{box-shadow: 0em -3em 0 0, 2em -2em 0 -1em, 3em 0 0 -1em, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 0, -3em 0em 0 0, -2em -2em 0 0.2em;}}\n" +
                        "/* sc-component-id: sc-keyframes-iXASsm */\n" +
                        "@-webkit-keyframes iXASsm{0%{box-shadow: 0 -0.83em 0 -0.4em, 0 -0.83em 0 -0.42em, 0 -0.83em 0 -0.44em, 0 -0.83em 0 -0.46em, 0 -0.83em 0 -0.477em;}5%,95%{box-shadow: 0 -0.83em 0 -0.4em, 0 -0.83em 0 -0.42em, 0 -0.83em 0 -0.44em, 0 -0.83em 0 -0.46em, 0 -0.83em 0 -0.477em;}10%,59%{box-shadow: 0 -0.83em 0 -0.4em, -0.087em -0.825em 0 -0.42em, -0.173em -0.812em 0 -0.44em, -0.256em -0.789em 0 -0.46em, -0.297em -0.775em 0 -0.477em;}20%{box-shadow: 0 -0.83em 0 -0.4em, -0.338em -0.758em 0 -0.42em, -0.555em -0.617em 0 -0.44em, -0.671em -0.488em 0 -0.46em, -0.749em -0.34em 0 -0.477em;}38%{box-shadow: 0 -0.83em 0 -0.4em, -0.377em -0.74em 0 -0.42em, -0.645em -0.522em 0 -0.44em, -0.775em -0.297em 0 -0.46em, -0.82em -0.09em 0 -0.477em;}100%{box-shadow: 0 -0.83em 0 -0.4em, 0 -0.83em 0 -0.42em, 0 -0.83em 0 -0.44em, 0 -0.83em 0 -0.46em, 0 -0.83em 0 -0.477em;}}@keyframes iXASsm{0%{box-shadow: 0 -0.83em 0 -0.4em, 0 -0.83em 0 -0.42em, 0 -0.83em 0 -0.44em, 0 -0.83em 0 -0.46em, 0 -0.83em 0 -0.477em;}5%,95%{box-shadow: 0 -0.83em 0 -0.4em, 0 -0.83em 0 -0.42em, 0 -0.83em 0 -0.44em, 0 -0.83em 0 -0.46em, 0 -0.83em 0 -0.477em;}10%,59%{box-shadow: 0 -0.83em 0 -0.4em, -0.087em -0.825em 0 -0.42em, -0.173em -0.812em 0 -0.44em, -0.256em -0.789em 0 -0.46em, -0.297em -0.775em 0 -0.477em;}20%{box-shadow: 0 -0.83em 0 -0.4em, -0.338em -0.758em 0 -0.42em, -0.555em -0.617em 0 -0.44em, -0.671em -0.488em 0 -0.46em, -0.749em -0.34em 0 -0.477em;}38%{box-shadow: 0 -0.83em 0 -0.4em, -0.377em -0.74em 0 -0.42em, -0.645em -0.522em 0 -0.44em, -0.775em -0.297em 0 -0.46em, -0.82em -0.09em 0 -0.477em;}100%{box-shadow: 0 -0.83em 0 -0.4em, 0 -0.83em 0 -0.42em, 0 -0.83em 0 -0.44em, 0 -0.83em 0 -0.46em, 0 -0.83em 0 -0.477em;}}\n" +
                        "/* sc-component-id: sc-keyframes-iECmZH */\n" +
                        "@-webkit-keyframes iECmZH{0%{-webkit-transform: rotate(0deg);-ms-transform: rotate(0deg);transform: rotate(0deg);}100%{-webkit-transform: rotate(360deg);-ms-transform: rotate(360deg);transform: rotate(360deg);}}@keyframes iECmZH{0%{-webkit-transform: rotate(0deg);-ms-transform: rotate(0deg);transform: rotate(0deg);}100%{-webkit-transform: rotate(360deg);-ms-transform: rotate(360deg);transform: rotate(360deg);}}\n" +
                        "/* sc-component-id: sc-bxivhb */\n" +
                        ".sc-bxivhb {}\n" +
                        ".bFMHxK{-webkit-animation:iXASsm 1.7s infinite ease,iECmZH 1.7s infinite ease;animation:iXASsm 1.7s infinite ease,iECmZH 1.7s infinite ease;border-radius: 50%;color: #000;font-size: 90px;height: 1em;margin: 72px auto;overflow: hidden;position: relative;text-indent: -9999em;-webkit-transform: translateZ(0);-ms-transform: translateZ(0);transform: translateZ(0);width: 1em;}</style><style type=\"text/css\">.MJXp-script {font-size: .8em}\n" +
                        ".MJXp-right {-webkit-transform-origin: right; -moz-transform-origin: right; -ms-transform-origin: right; -o-transform-origin: right; transform-origin: right}\n" +
                        ".MJXp-bold {font-weight: bold}\n" +
                        ".MJXp-italic {font-style: italic}\n" +
                        ".MJXp-scr {font-family: MathJax_Script,'Times New Roman',Times,STIXGeneral,serif}\n" +
                        ".MJXp-frak {font-family: MathJax_Fraktur,'Times New Roman',Times,STIXGeneral,serif}\n" +
                        ".MJXp-sf {font-family: MathJax_SansSerif,'Times New Roman',Times,STIXGeneral,serif}\n" +
                        ".MJXp-cal {font-family: MathJax_Caligraphic,'Times New Roman',Times,STIXGeneral,serif}\n" +
                        ".MJXp-mono {font-family: MathJax_Typewriter,'Times New Roman',Times,STIXGeneral,serif}\n" +
                        ".MJXp-largeop {font-size: 150%}\n" +
                        ".MJXp-largeop.MJXp-int {vertical-align: -.2em}\n" +
                        ".MJXp-math {display: inline-block; line-height: 1.2; text-indent: 0; font-family: 'Times New Roman',Times,STIXGeneral,serif; white-space: nowrap; border-collapse: collapse}\n" +
                        ".MJXp-display {display: block; text-align: center; margin: 1em 0}\n" +
                        ".MJXp-math span {display: inline-block}\n" +
                        ".MJXp-box {display: block!important; text-align: center}\n" +
                        ".MJXp-box:after {content: \" \"}\n" +
                        ".MJXp-rule {display: block!important; margin-top: .1em}\n" +
                        ".MJXp-char {display: block!important}\n" +
                        ".MJXp-mo {margin: 0 .15em}\n" +
                        ".MJXp-mfrac {margin: 0 .125em; vertical-align: .25em}\n" +
                        ".MJXp-denom {display: inline-table!important; width: 100%}\n" +
                        ".MJXp-denom > * {display: table-row!important}\n" +
                        ".MJXp-surd {vertical-align: top}\n" +
                        ".MJXp-surd > * {display: block!important}\n" +
                        ".MJXp-script-box > *  {display: table!important; height: 50%}\n" +
                        ".MJXp-script-box > * > * {display: table-cell!important; vertical-align: top}\n" +
                        ".MJXp-script-box > *:last-child > * {vertical-align: bottom}\n" +
                        ".MJXp-script-box > * > * > * {display: block!important}\n" +
                        ".MJXp-mphantom {visibility: hidden}\n" +
                        ".MJXp-munderover {display: inline-table!important}\n" +
                        ".MJXp-over {display: inline-block!important; text-align: center}\n" +
                        ".MJXp-over > * {display: block!important}\n" +
                        ".MJXp-munderover > * {display: table-row!important}\n" +
                        ".MJXp-mtable {vertical-align: .25em; margin: 0 .125em}\n" +
                        ".MJXp-mtable > * {display: inline-table!important; vertical-align: middle}\n" +
                        ".MJXp-mtr {display: table-row!important}\n" +
                        ".MJXp-mtd {display: table-cell!important; text-align: center; padding: .5em 0 0 .5em}\n" +
                        ".MJXp-mtr > .MJXp-mtd:first-child {padding-left: 0}\n" +
                        ".MJXp-mtr:first-child > .MJXp-mtd {padding-top: 0}\n" +
                        ".MJXp-mlabeledtr {display: table-row!important}\n" +
                        ".MJXp-mlabeledtr > .MJXp-mtd:first-child {padding-left: 0}\n" +
                        ".MJXp-mlabeledtr:first-child > .MJXp-mtd {padding-top: 0}\n" +
                        ".MJXp-merror {background-color: #FFFF88; color: #CC0000; border: 1px solid #CC0000; padding: 1px 3px; font-style: normal; font-size: 90%}\n" +
                        ".MJXp-scale0 {-webkit-transform: scaleX(.0); -moz-transform: scaleX(.0); -ms-transform: scaleX(.0); -o-transform: scaleX(.0); transform: scaleX(.0)}\n" +
                        ".MJXp-scale1 {-webkit-transform: scaleX(.1); -moz-transform: scaleX(.1); -ms-transform: scaleX(.1); -o-transform: scaleX(.1); transform: scaleX(.1)}\n" +
                        ".MJXp-scale2 {-webkit-transform: scaleX(.2); -moz-transform: scaleX(.2); -ms-transform: scaleX(.2); -o-transform: scaleX(.2); transform: scaleX(.2)}\n" +
                        ".MJXp-scale3 {-webkit-transform: scaleX(.3); -moz-transform: scaleX(.3); -ms-transform: scaleX(.3); -o-transform: scaleX(.3); transform: scaleX(.3)}\n" +
                        ".MJXp-scale4 {-webkit-transform: scaleX(.4); -moz-transform: scaleX(.4); -ms-transform: scaleX(.4); -o-transform: scaleX(.4); transform: scaleX(.4)}\n" +
                        ".MJXp-scale5 {-webkit-transform: scaleX(.5); -moz-transform: scaleX(.5); -ms-transform: scaleX(.5); -o-transform: scaleX(.5); transform: scaleX(.5)}\n" +
                        ".MJXp-scale6 {-webkit-transform: scaleX(.6); -moz-transform: scaleX(.6); -ms-transform: scaleX(.6); -o-transform: scaleX(.6); transform: scaleX(.6)}\n" +
                        ".MJXp-scale7 {-webkit-transform: scaleX(.7); -moz-transform: scaleX(.7); -ms-transform: scaleX(.7); -o-transform: scaleX(.7); transform: scaleX(.7)}\n" +
                        ".MJXp-scale8 {-webkit-transform: scaleX(.8); -moz-transform: scaleX(.8); -ms-transform: scaleX(.8); -o-transform: scaleX(.8); transform: scaleX(.8)}\n" +
                        ".MJXp-scale9 {-webkit-transform: scaleX(.9); -moz-transform: scaleX(.9); -ms-transform: scaleX(.9); -o-transform: scaleX(.9); transform: scaleX(.9)}\n" +
                        ".MathJax_PHTML .noError {vertical-align: ; font-size: 90%; text-align: left; color: black; padding: 1px 3px; border: 1px solid}\n" +
                        "</style><style type=\"text/css\">.mjx-chtml {display: inline-block; line-height: 0; text-indent: 0; text-align: left; text-transform: none; font-style: normal; font-weight: normal; font-size: 100%; font-size-adjust: none; letter-spacing: normal; word-wrap: normal; word-spacing: normal; white-space: nowrap; float: none; direction: ltr; max-width: none; max-height: none; min-width: 0; min-height: 0; border: 0; margin: 0; padding: 1px 0}\n" +
                        ".MJXc-display {display: block; text-align: center; margin: 1em 0; padding: 0}\n" +
                        ".mjx-chtml[tabindex]:focus, body :focus .mjx-chtml[tabindex] {display: inline-table}\n" +
                        ".mjx-full-width {text-align: center; display: table-cell!important; width: 10000em}\n" +
                        ".mjx-math {display: inline-block; border-collapse: separate; border-spacing: 0}\n" +
                        ".mjx-math * {display: inline-block; -webkit-box-sizing: content-box!important; -moz-box-sizing: content-box!important; box-sizing: content-box!important; text-align: left}\n" +
                        ".mjx-numerator {display: block; text-align: center}\n" +
                        ".mjx-denominator {display: block; text-align: center}\n" +
                        ".MJXc-stacked {height: 0; position: relative}\n" +
                        ".MJXc-stacked > * {position: absolute}\n" +
                        ".MJXc-bevelled > * {display: inline-block}\n" +
                        ".mjx-stack {display: inline-block}\n" +
                        ".mjx-op {display: block}\n" +
                        ".mjx-under {display: table-cell}\n" +
                        ".mjx-over {display: block}\n" +
                        ".mjx-over > * {padding-left: 0px!important; padding-right: 0px!important}\n" +
                        ".mjx-under > * {padding-left: 0px!important; padding-right: 0px!important}\n" +
                        ".mjx-stack > .mjx-sup {display: block}\n" +
                        ".mjx-stack > .mjx-sub {display: block}\n" +
                        ".mjx-prestack > .mjx-presup {display: block}\n" +
                        ".mjx-prestack > .mjx-presub {display: block}\n" +
                        ".mjx-delim-h > .mjx-char {display: inline-block}\n" +
                        ".mjx-surd {vertical-align: top}\n" +
                        ".mjx-mphantom * {visibility: hidden}\n" +
                        ".mjx-merror {background-color: #FFFF88; color: #CC0000; border: 1px solid #CC0000; padding: 2px 3px; font-style: normal; font-size: 90%}\n" +
                        ".mjx-annotation-xml {line-height: normal}\n" +
                        ".mjx-menclose > svg {fill: none; stroke: currentColor}\n" +
                        ".mjx-mtr {display: table-row}\n" +
                        ".mjx-mlabeledtr {display: table-row}\n" +
                        ".mjx-mtd {display: table-cell; text-align: center}\n" +
                        ".mjx-label {display: table-row}\n" +
                        ".mjx-box {display: inline-block}\n" +
                        ".mjx-block {display: block}\n" +
                        ".mjx-span {display: inline}\n" +
                        ".mjx-char {display: block; white-space: pre}\n" +
                        ".mjx-itable {display: inline-table; width: auto}\n" +
                        ".mjx-row {display: table-row}\n" +
                        ".mjx-cell {display: table-cell}\n" +
                        ".mjx-table {display: table; width: 100%}\n" +
                        ".mjx-line {display: block; height: 0}\n" +
                        ".mjx-strut {width: 0; padding-top: 1em}\n" +
                        ".mjx-vsize {width: 0}\n" +
                        ".MJXc-space1 {margin-left: .167em}\n" +
                        ".MJXc-space2 {margin-left: .222em}\n" +
                        ".MJXc-space3 {margin-left: .278em}\n" +
                        ".mjx-chartest {display: block; visibility: hidden; position: absolute; top: 0; line-height: normal; font-size: 500%}\n" +
                        ".mjx-chartest .mjx-char {display: inline}\n" +
                        ".mjx-chartest .mjx-box {padding-top: 1000px}\n" +
                        ".MJXc-processing {visibility: hidden; position: fixed; width: 0; height: 0; overflow: hidden}\n" +
                        ".MJXc-processed {display: none}\n" +
                        ".mjx-test {display: block; font-style: normal; font-weight: normal; font-size: 100%; font-size-adjust: none; text-indent: 0; text-transform: none; letter-spacing: normal; word-spacing: normal; overflow: hidden; height: 1px}\n" +
                        ".mjx-ex-box-test {position: absolute; width: 1px; height: 60ex}\n" +
                        ".mjx-line-box-test {display: table!important}\n" +
                        ".mjx-line-box-test span {display: table-cell!important; width: 10000em!important; min-width: 0; max-width: none; padding: 0; border: 0; margin: 0}\n" +
                        "#MathJax_CHTML_Tooltip {background-color: InfoBackground; color: InfoText; border: 1px solid black; box-shadow: 2px 2px 5px #AAAAAA; -webkit-box-shadow: 2px 2px 5px #AAAAAA; -moz-box-shadow: 2px 2px 5px #AAAAAA; -khtml-box-shadow: 2px 2px 5px #AAAAAA; padding: 3px 4px; z-index: 401; position: absolute; left: 0; top: 0; width: auto; height: auto; display: none}\n" +
                        ".mjx-chtml .mjx-noError {line-height: 1.2; vertical-align: ; font-size: 90%; text-align: left; color: black; padding: 1px 3px; border: 1px solid}\n" +
                        ".MJXc-TeX-unknown-R {font-family: STIXGeneral,'Cambria Math','Arial Unicode MS',serif; font-style: normal; font-weight: normal}\n" +
                        ".MJXc-TeX-unknown-I {font-family: STIXGeneral,'Cambria Math','Arial Unicode MS',serif; font-style: italic; font-weight: normal}\n" +
                        ".MJXc-TeX-unknown-B {font-family: STIXGeneral,'Cambria Math','Arial Unicode MS',serif; font-style: normal; font-weight: bold}\n" +
                        ".MJXc-TeX-unknown-BI {font-family: STIXGeneral,'Cambria Math','Arial Unicode MS',serif; font-style: italic; font-weight: bold}\n" +
                        ".MJXc-TeX-ams-R {font-family: MJXc-TeX-ams-R,MJXc-TeX-ams-Rw}\n" +
                        ".MJXc-TeX-cal-B {font-family: MJXc-TeX-cal-B,MJXc-TeX-cal-Bx,MJXc-TeX-cal-Bw}\n" +
                        ".MJXc-TeX-frak-R {font-family: MJXc-TeX-frak-R,MJXc-TeX-frak-Rw}\n" +
                        ".MJXc-TeX-frak-B {font-family: MJXc-TeX-frak-B,MJXc-TeX-frak-Bx,MJXc-TeX-frak-Bw}\n" +
                        ".MJXc-TeX-math-BI {font-family: MJXc-TeX-math-BI,MJXc-TeX-math-BIx,MJXc-TeX-math-BIw}\n" +
                        ".MJXc-TeX-sans-R {font-family: MJXc-TeX-sans-R,MJXc-TeX-sans-Rw}\n" +
                        ".MJXc-TeX-sans-B {font-family: MJXc-TeX-sans-B,MJXc-TeX-sans-Bx,MJXc-TeX-sans-Bw}\n" +
                        ".MJXc-TeX-sans-I {font-family: MJXc-TeX-sans-I,MJXc-TeX-sans-Ix,MJXc-TeX-sans-Iw}\n" +
                        ".MJXc-TeX-script-R {font-family: MJXc-TeX-script-R,MJXc-TeX-script-Rw}\n" +
                        ".MJXc-TeX-type-R {font-family: MJXc-TeX-type-R,MJXc-TeX-type-Rw}\n" +
                        ".MJXc-TeX-cal-R {font-family: MJXc-TeX-cal-R,MJXc-TeX-cal-Rw}\n" +
                        ".MJXc-TeX-main-B {font-family: MJXc-TeX-main-B,MJXc-TeX-main-Bx,MJXc-TeX-main-Bw}\n" +
                        ".MJXc-TeX-main-I {font-family: MJXc-TeX-main-I,MJXc-TeX-main-Ix,MJXc-TeX-main-Iw}\n" +
                        ".MJXc-TeX-main-R {font-family: MJXc-TeX-main-R,MJXc-TeX-main-Rw}\n" +
                        ".MJXc-TeX-math-I {font-family: MJXc-TeX-math-I,MJXc-TeX-math-Ix,MJXc-TeX-math-Iw}\n" +
                        ".MJXc-TeX-size1-R {font-family: MJXc-TeX-size1-R,MJXc-TeX-size1-Rw}\n" +
                        ".MJXc-TeX-size2-R {font-family: MJXc-TeX-size2-R,MJXc-TeX-size2-Rw}\n" +
                        ".MJXc-TeX-size3-R {font-family: MJXc-TeX-size3-R,MJXc-TeX-size3-Rw}\n" +
                        ".MJXc-TeX-size4-R {font-family: MJXc-TeX-size4-R,MJXc-TeX-size4-Rw}\n" +
                        "@font-face {font-family: MJXc-TeX-ams-R; src: local('MathJax_AMS'), local('MathJax_AMS-Regular')}\n" +
                        "@font-face {font-family: MJXc-TeX-ams-Rw; src /*1*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/eot/MathJax_AMS-Regular.eot'); src /*2*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/woff/MathJax_AMS-Regular.woff') format('woff'), url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/otf/MathJax_AMS-Regular.otf') format('opentype')}\n" +
                        "@font-face {font-family: MJXc-TeX-cal-B; src: local('MathJax_Caligraphic Bold'), local('MathJax_Caligraphic-Bold')}\n" +
                        "@font-face {font-family: MJXc-TeX-cal-Bx; src: local('MathJax_Caligraphic'); font-weight: bold}\n" +
                        "@font-face {font-family: MJXc-TeX-cal-Bw; src /*1*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/eot/MathJax_Caligraphic-Bold.eot'); src /*2*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/woff/MathJax_Caligraphic-Bold.woff') format('woff'), url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/otf/MathJax_Caligraphic-Bold.otf') format('opentype')}\n" +
                        "@font-face {font-family: MJXc-TeX-frak-R; src: local('MathJax_Fraktur'), local('MathJax_Fraktur-Regular')}\n" +
                        "@font-face {font-family: MJXc-TeX-frak-Rw; src /*1*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/eot/MathJax_Fraktur-Regular.eot'); src /*2*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/woff/MathJax_Fraktur-Regular.woff') format('woff'), url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/otf/MathJax_Fraktur-Regular.otf') format('opentype')}\n" +
                        "@font-face {font-family: MJXc-TeX-frak-B; src: local('MathJax_Fraktur Bold'), local('MathJax_Fraktur-Bold')}\n" +
                        "@font-face {font-family: MJXc-TeX-frak-Bx; src: local('MathJax_Fraktur'); font-weight: bold}\n" +
                        "@font-face {font-family: MJXc-TeX-frak-Bw; src /*1*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/eot/MathJax_Fraktur-Bold.eot'); src /*2*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/woff/MathJax_Fraktur-Bold.woff') format('woff'), url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/otf/MathJax_Fraktur-Bold.otf') format('opentype')}\n" +
                        "@font-face {font-family: MJXc-TeX-math-BI; src: local('MathJax_Math BoldItalic'), local('MathJax_Math-BoldItalic')}\n" +
                        "@font-face {font-family: MJXc-TeX-math-BIx; src: local('MathJax_Math'); font-weight: bold; font-style: italic}\n" +
                        "@font-face {font-family: MJXc-TeX-math-BIw; src /*1*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/eot/MathJax_Math-BoldItalic.eot'); src /*2*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/woff/MathJax_Math-BoldItalic.woff') format('woff'), url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/otf/MathJax_Math-BoldItalic.otf') format('opentype')}\n" +
                        "@font-face {font-family: MJXc-TeX-sans-R; src: local('MathJax_SansSerif'), local('MathJax_SansSerif-Regular')}\n" +
                        "@font-face {font-family: MJXc-TeX-sans-Rw; src /*1*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/eot/MathJax_SansSerif-Regular.eot'); src /*2*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/woff/MathJax_SansSerif-Regular.woff') format('woff'), url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/otf/MathJax_SansSerif-Regular.otf') format('opentype')}\n" +
                        "@font-face {font-family: MJXc-TeX-sans-B; src: local('MathJax_SansSerif Bold'), local('MathJax_SansSerif-Bold')}\n" +
                        "@font-face {font-family: MJXc-TeX-sans-Bx; src: local('MathJax_SansSerif'); font-weight: bold}\n" +
                        "@font-face {font-family: MJXc-TeX-sans-Bw; src /*1*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/eot/MathJax_SansSerif-Bold.eot'); src /*2*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/woff/MathJax_SansSerif-Bold.woff') format('woff'), url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/otf/MathJax_SansSerif-Bold.otf') format('opentype')}\n" +
                        "@font-face {font-family: MJXc-TeX-sans-I; src: local('MathJax_SansSerif Italic'), local('MathJax_SansSerif-Italic')}\n" +
                        "@font-face {font-family: MJXc-TeX-sans-Ix; src: local('MathJax_SansSerif'); font-style: italic}\n" +
                        "@font-face {font-family: MJXc-TeX-sans-Iw; src /*1*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/eot/MathJax_SansSerif-Italic.eot'); src /*2*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/woff/MathJax_SansSerif-Italic.woff') format('woff'), url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/otf/MathJax_SansSerif-Italic.otf') format('opentype')}\n" +
                        "@font-face {font-family: MJXc-TeX-script-R; src: local('MathJax_Script'), local('MathJax_Script-Regular')}\n" +
                        "@font-face {font-family: MJXc-TeX-script-Rw; src /*1*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/eot/MathJax_Script-Regular.eot'); src /*2*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/woff/MathJax_Script-Regular.woff') format('woff'), url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/otf/MathJax_Script-Regular.otf') format('opentype')}\n" +
                        "@font-face {font-family: MJXc-TeX-type-R; src: local('MathJax_Typewriter'), local('MathJax_Typewriter-Regular')}\n" +
                        "@font-face {font-family: MJXc-TeX-type-Rw; src /*1*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/eot/MathJax_Typewriter-Regular.eot'); src /*2*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/woff/MathJax_Typewriter-Regular.woff') format('woff'), url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/otf/MathJax_Typewriter-Regular.otf') format('opentype')}\n" +
                        "@font-face {font-family: MJXc-TeX-cal-R; src: local('MathJax_Caligraphic'), local('MathJax_Caligraphic-Regular')}\n" +
                        "@font-face {font-family: MJXc-TeX-cal-Rw; src /*1*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/eot/MathJax_Caligraphic-Regular.eot'); src /*2*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/woff/MathJax_Caligraphic-Regular.woff') format('woff'), url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/otf/MathJax_Caligraphic-Regular.otf') format('opentype')}\n" +
                        "@font-face {font-family: MJXc-TeX-main-B; src: local('MathJax_Main Bold'), local('MathJax_Main-Bold')}\n" +
                        "@font-face {font-family: MJXc-TeX-main-Bx; src: local('MathJax_Main'); font-weight: bold}\n" +
                        "@font-face {font-family: MJXc-TeX-main-Bw; src /*1*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/eot/MathJax_Main-Bold.eot'); src /*2*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/woff/MathJax_Main-Bold.woff') format('woff'), url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/otf/MathJax_Main-Bold.otf') format('opentype')}\n" +
                        "@font-face {font-family: MJXc-TeX-main-I; src: local('MathJax_Main Italic'), local('MathJax_Main-Italic')}\n" +
                        "@font-face {font-family: MJXc-TeX-main-Ix; src: local('MathJax_Main'); font-style: italic}\n" +
                        "@font-face {font-family: MJXc-TeX-main-Iw; src /*1*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/eot/MathJax_Main-Italic.eot'); src /*2*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/woff/MathJax_Main-Italic.woff') format('woff'), url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/otf/MathJax_Main-Italic.otf') format('opentype')}\n" +
                        "@font-face {font-family: MJXc-TeX-main-R; src: local('MathJax_Main'), local('MathJax_Main-Regular')}\n" +
                        "@font-face {font-family: MJXc-TeX-main-Rw; src /*1*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/eot/MathJax_Main-Regular.eot'); src /*2*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/woff/MathJax_Main-Regular.woff') format('woff'), url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/otf/MathJax_Main-Regular.otf') format('opentype')}\n" +
                        "@font-face {font-family: MJXc-TeX-math-I; src: local('MathJax_Math Italic'), local('MathJax_Math-Italic')}\n" +
                        "@font-face {font-family: MJXc-TeX-math-Ix; src: local('MathJax_Math'); font-style: italic}\n" +
                        "@font-face {font-family: MJXc-TeX-math-Iw; src /*1*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/eot/MathJax_Math-Italic.eot'); src /*2*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/woff/MathJax_Math-Italic.woff') format('woff'), url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/otf/MathJax_Math-Italic.otf') format('opentype')}\n" +
                        "@font-face {font-family: MJXc-TeX-size1-R; src: local('MathJax_Size1'), local('MathJax_Size1-Regular')}\n" +
                        "@font-face {font-family: MJXc-TeX-size1-Rw; src /*1*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/eot/MathJax_Size1-Regular.eot'); src /*2*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/woff/MathJax_Size1-Regular.woff') format('woff'), url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/otf/MathJax_Size1-Regular.otf') format('opentype')}\n" +
                        "@font-face {font-family: MJXc-TeX-size2-R; src: local('MathJax_Size2'), local('MathJax_Size2-Regular')}\n" +
                        "@font-face {font-family: MJXc-TeX-size2-Rw; src /*1*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/eot/MathJax_Size2-Regular.eot'); src /*2*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/woff/MathJax_Size2-Regular.woff') format('woff'), url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/otf/MathJax_Size2-Regular.otf') format('opentype')}\n" +
                        "@font-face {font-family: MJXc-TeX-size3-R; src: local('MathJax_Size3'), local('MathJax_Size3-Regular')}\n" +
                        "@font-face {font-family: MJXc-TeX-size3-Rw; src /*1*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/eot/MathJax_Size3-Regular.eot'); src /*2*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/woff/MathJax_Size3-Regular.woff') format('woff'), url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/otf/MathJax_Size3-Regular.otf') format('opentype')}\n" +
                        "@font-face {font-family: MJXc-TeX-size4-R; src: local('MathJax_Size4'), local('MathJax_Size4-Regular')}\n" +
                        "@font-face {font-family: MJXc-TeX-size4-Rw; src /*1*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/eot/MathJax_Size4-Regular.eot'); src /*2*/: url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/woff/MathJax_Size4-Regular.woff') format('woff'), url('https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/fonts/HTML-CSS/TeX/otf/MathJax_Size4-Regular.otf') format('opentype')}\n" +
                        "</style></head>";
                String basicUrl = "https://algoprog.ru/material/" + id;
                String content = response.body().getContent();
                browser.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
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
