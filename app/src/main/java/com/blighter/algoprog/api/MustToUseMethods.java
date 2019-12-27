package com.blighter.algoprog.api;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.widget.Toast;

import com.blighter.algoprog.R;
import com.blighter.algoprog.pojo.Cookies;
import com.blighter.algoprog.pojo.UserData;
import com.blighter.algoprog.pojo.myUser;
import com.blighter.algoprog.retrofit.AuthorizationInterface;
import com.blighter.algoprog.retrofit.Client;
import com.blighter.algoprog.retrofit.MyUserInterface;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.blighter.algoprog.api.ApiMethods.getBestSolutions;
import static com.blighter.algoprog.api.ApiMethods.getSolutions;

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

    public static CompositeDisposable getCookiesAndMyUser(Context context, final android.support.v7.app.ActionBar actionBar, UserData userData, NavigationView navView, Boolean firstTimeLoggingIn) {
        AuthorizationInterface apiForCookies = Client.getClient().create(AuthorizationInterface.class);
        MyUserInterface apiForMyUser = Client.getClient().create(MyUserInterface.class);
        CompositeDisposable disposables = new CompositeDisposable();
        final boolean[] cookiesOnSuccess = {true};
        apiForCookies.getCookies(userData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .switchMap((Function<Response<Cookies>, Observable<myUser>>) cookiesResponse -> {
                    if (!cookiesResponse.isSuccessful()) {
                        cookiesOnSuccess[0] = false;
                        return null;
                    } else {
                        String[] cookiesAndSomething = cookiesResponse.headers().get("Set-Cookie").split(";");
                        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_preferences), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(context.getString(R.string.cookies), cookiesAndSomething[0]).apply();
                        return apiForMyUser.getMyUserInfo(cookiesAndSomething[0]);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<myUser>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(myUser myUserResponse) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_preferences),Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(context.getString(R.string.user_id),"").apply();
                        SpannableStringBuilder coolSubtitle = new SpannableStringBuilder();
                        String name = myUserResponse.getName();
                        String rating = myUserResponse.getRating().toString();
                        double fullActivity = myUserResponse.getActivity();
                        int firstPartOfActivity = (int) fullActivity;
                        int secondPartOfActivity = (int) ((fullActivity * 10) % 10);
                        String activity = firstPartOfActivity + "." + secondPartOfActivity;
                        SpannableString coolSubtitlePartOne = new SpannableString(rating);
                        SpannableString coolSubtitlePartTwo = new SpannableString("/" + activity);
                        coolSubtitlePartOne.setSpan(new ForegroundColorSpan(Color.HSVToColor(getHSV(myUserResponse.getRating(), myUserResponse.getActivity()))), 0, rating.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        coolSubtitle.append(coolSubtitlePartOne);
                        coolSubtitlePartTwo.setSpan(new ForegroundColorSpan(Color.BLACK), 0, activity.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        coolSubtitle.append(coolSubtitlePartTwo);
                        SpannableString coolTitle = new SpannableString(myUserResponse.getName() + "  " + myUserResponse.getLevel().getCurrent());
                        coolTitle.setSpan(new ForegroundColorSpan(Color.HSVToColor(getHSV(myUserResponse.getRating(), myUserResponse.getActivity()))), 0, name.length(), 0);
                        actionBar.setTitle(coolTitle);
                        actionBar.setSubtitle(coolSubtitle);
                        Menu menu = navView.getMenu();
                        menu.add(R.id.settings_and_enter, R.id.nav_enter + 200, 2, R.string.change_user).setIcon(R.drawable.ic_menu_import_export_black);
                        menu.add(R.id.settings_and_enter, R.id.nav_enter + 100, 2, R.string.exit).setIcon(R.drawable.ic_menu_export_black);
                        menu.removeItem(R.id.nav_enter);
                        navView.invalidate();
                        if (firstTimeLoggingIn) {
                            ((Activity) context).onBackPressed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (cookiesOnSuccess[0]) {
                            Toast.makeText(context, "Неизвестная ошибка", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
                        }
                        ((FragmentActivity) context).onBackPressed();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
        return disposables;
    }

    private static CompositeDisposable getMyUser(String cookies, final android.support.v7.app.ActionBar actionBar, Context context) {
        CompositeDisposable disposables = new CompositeDisposable();
        MyUserInterface apiForMyUser = Client.getClient().create(MyUserInterface.class);
        apiForMyUser.getMyUserInfo(cookies)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<myUser>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(myUser myUserResponse) {
                        SpannableStringBuilder coolSubtitle = new SpannableStringBuilder();
                        String name = myUserResponse.getName();
                        String rating = myUserResponse.getRating().toString();
                        double fullActivity = myUserResponse.getActivity();
                        int firstPartOfActivity = (int) fullActivity;
                        int secondPartOfActivity = (int) ((fullActivity * 10) % 10);
                        String activity = firstPartOfActivity + "." + secondPartOfActivity;
                        SpannableString coolSubtitlePartOne = new SpannableString(rating);
                        SpannableString coolSubtitlePartTwo = new SpannableString("/" + activity);
                        coolSubtitlePartOne.setSpan(new ForegroundColorSpan(Color.HSVToColor(getHSV(myUserResponse.getRating(), myUserResponse.getActivity()))), 0, rating.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        coolSubtitle.append(coolSubtitlePartOne);
                        coolSubtitlePartTwo.setSpan(new ForegroundColorSpan(Color.BLACK), 0, activity.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        coolSubtitle.append(coolSubtitlePartTwo);
                        SpannableString coolTitle = new SpannableString(myUserResponse.getName() + "  " + myUserResponse.getLevel().getCurrent());
                        coolTitle.setSpan(new ForegroundColorSpan(Color.HSVToColor(getHSV(myUserResponse.getRating(), myUserResponse.getActivity()))), 0, name.length(), 0);
                        actionBar.setTitle(coolTitle);
                        actionBar.setSubtitle(coolSubtitle);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, "Неизвестная ошибка при обновление информации о пользователе", Toast.LENGTH_LONG).show();
                        ((FragmentActivity) context).onBackPressed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return disposables;
    }

    public static CompositeDisposable setNiceTitle(android.support.v7.app.ActionBar actionBar, Context context, NavigationView navigationView) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.app_preferences), Context.MODE_PRIVATE);
        boolean secondLevelAuthorized = sharedPref.getBoolean(context.getString(R.string.second_level_authorization), false);
        boolean firstLevelAuthorized = sharedPref.getBoolean(context.getString(R.string.first_level_authorization), false);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        if (firstLevelAuthorized) {
            compositeDisposable = getMyUser(sharedPref.getString(context.getString(R.string.cookies), ""), actionBar, context);
        } else if (secondLevelAuthorized) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(context.getString(R.string.first_level_authorization), true);
            editor.apply();
            compositeDisposable = getCookiesAndMyUser(context, actionBar, new UserData(sharedPref.getString(context.getString(R.string.login), ""), sharedPref.getString(context.getString(R.string.password), "")), navigationView, false);
        } else
            actionBar.setTitle("Неизвестный пользователь");
        return compositeDisposable;
    }
}
