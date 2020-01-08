package com.blighter.algoprog.api;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.room.Room;

import com.blighter.algoprog.R;
import com.blighter.algoprog.data.Achievement;
import com.blighter.algoprog.data.AchievementDao;
import com.blighter.algoprog.data.Database;
import com.blighter.algoprog.pojo.Cookies;
import com.blighter.algoprog.pojo.UserData;
import com.blighter.algoprog.pojo.myUser;
import com.blighter.algoprog.retrofit.AuthorizationInterface;
import com.blighter.algoprog.retrofit.Client;
import com.blighter.algoprog.retrofit.MyUserInterface;
import com.blighter.algoprog.utils.RoundedBackgroundSpan;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

//this class contains methods which are interact with ActionBar and cookies and could be used in several places
public class LoginMethods {
    //this method using a formula that Kalinin created to generate color for user name
    public static float[] getHSV(int rating, double activity) {
        double R = 160000.0;
        double A = 7.0;
        double h = 11.0 / 12.0 * (1.0 - Math.log(rating + 1.0) / Math.log(R + 1.0));
        double v = 0.3 + 0.7 * Math.log(activity + 1.0) / Math.log(A + 1.0);
        if (0.1 > activity) {
            v = 0;
        }
        h *= 366;
        float h1 = (float) h;
        float v1 = (float) v;
        return new float[]{h1, 100, v1};

    }

    //This method gets new Cookies, then getting user info, building string for ActionBar using got info and setting built strings
    public static CompositeDisposable getCookiesAndMyUser(Context context, final androidx.appcompat.app.ActionBar actionBar, UserData userData, NavigationView navView, Boolean firstTimeLoggingIn, @Nullable TextInputLayout textInputLayout) {
        AuthorizationInterface apiForCookies = Client.getClient().create(AuthorizationInterface.class);
        MyUserInterface apiForMyUser = Client.getClient().create(MyUserInterface.class);
        CompositeDisposable disposables = new CompositeDisposable();
        final boolean[] cookiesOnSuccess = {true};
        //getting cookies using userData
        apiForCookies.getCookies(userData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .switchMap((Function<Response<Cookies>, Observable<myUser>>) cookiesResponse -> {
                    //if we got wrong userData we return null and set cookiesOnSuccess to false
                    if (!cookiesResponse.isSuccessful()) {
                        cookiesOnSuccess[0] = false;
                        return null;
                    } else {
                        //getting cookies from headers
                        String[] cookiesAndSomething = cookiesResponse.headers().get("Set-Cookie").split(";");
                        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_preferences), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(context.getString(R.string.cookies), cookiesAndSomething[0]).apply();
                        //this is another network request with our cookies to get information about user
                        return apiForMyUser.getMyUserInfo(cookiesAndSomething[0]);
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .switchMap(new Function<myUser, Observable<Pair<SpannableString, SpannableStringBuilder>>>() {
                    @Override
                    public Observable<Pair<SpannableString, SpannableStringBuilder>> apply(myUser myUserResponse) throws Exception {
                        //building 2 strings: title and subtitle
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
                        coolSubtitle.append(" ");
                        SpannableString coolTitle = new SpannableString(myUserResponse.getName() + "  " + myUserResponse.getLevel().getCurrent());
                        coolTitle.setSpan(new ForegroundColorSpan(Color.HSVToColor(getHSV(myUserResponse.getRating(), myUserResponse.getActivity()))), 0, name.length(), 0);
                        //getting database instance from assets to to set achievements in subtitle
                        Database db = Room.databaseBuilder(context, Database.class, "achievements.db")
                                .fallbackToDestructiveMigration()
                                .createFromAsset("databases/achievements.db")
                                .build();
                        AchievementDao dao = db.getDao();
                        //adding achievements to subTitle
                        for (int i = 0; i < 3; i++) {
                            Achievement achievement1 = dao.getById(myUserResponse.getAchieves().get(i));
                            SpannableString text = new SpannableString(achievement1.getText());
                            text.setSpan(new RoundedBackgroundSpan(Color.parseColor(achievement1.getColor())), 0, text.length(), 0);
                            coolSubtitle.append(text);
                            coolSubtitle.append(" ");
                        }
                        //if we dont have userId we insert it
                        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_preferences), Context.MODE_PRIVATE);
                        if (!sharedPreferences.contains(context.getString(R.string.user_id))) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(context.getString(R.string.user_id), myUserResponse.get_id()).apply();
                        }
                        //creating Observable with 2 string, this made because
                        //we want to set those string on MainThread but build strings on background thread
                        Observable<Pair<SpannableString, SpannableStringBuilder>> strings = Observable
                                .create((ObservableOnSubscribe<Pair<SpannableString, SpannableStringBuilder>>) emitter -> {
                                    if (!emitter.isDisposed()) {
                                        Pair<SpannableString, SpannableStringBuilder> titles = new Pair<>(coolTitle, coolSubtitle);
                                        emitter.onNext(titles);
                                        emitter.onComplete();
                                    }
                                });
                        return strings;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Pair<SpannableString, SpannableStringBuilder>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(Pair<SpannableString, SpannableStringBuilder> titles) {
                        //if we arrived here it means there are no errors occurred and we can set
                        //login and password + the fact that we got new cookies
                        SharedPreferences data = context.getSharedPreferences(context.getString(R.string.app_preferences), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = data.edit();
                        editor.putString(context.getString(R.string.login), userData.getUsername());
                        editor.putString(context.getString(R.string.password), userData.getPassword());
                        editor.putBoolean(context.getString(R.string.oldCookies), false);
                        editor.putBoolean(context.getString(R.string.noCookies), false);
                        editor.apply();
                        actionBar.setTitle(titles.first);
                        actionBar.setSubtitle(titles.second);
                        Menu menu = navView.getMenu();
                        menu.clear();
                        navView.inflateMenu(R.menu.drawer_logged);
                        navView.invalidate();
                        if (firstTimeLoggingIn) {
                            ((Activity) context).onBackPressed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("сука", e.getMessage());
                        SharedPreferences data = context.getSharedPreferences(context.getString(R.string.app_preferences), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = data.edit();
                        editor.putBoolean(context.getString(R.string.oldCookies), true);
                        editor.putBoolean(context.getString(R.string.noCookies), true);
                        editor.apply();
                        if (cookiesOnSuccess[0]) {
                            textInputLayout.setError("Неизвестная ошибка");
                        } else {
                            textInputLayout.setError("Неверный логин или пароль");
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
        return disposables;
    }

    //basically the same as getCookiesAndMyUser but we already have cookies and dont do network request to get them
    private static CompositeDisposable getMyUser(String cookies, final androidx.appcompat.app.ActionBar actionBar, Context context) {
        CompositeDisposable disposables = new CompositeDisposable();
        MyUserInterface apiForMyUser = Client.getClient().create(MyUserInterface.class);
        apiForMyUser.getMyUserInfo(cookies)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .switchMap((Function<myUser, Observable<Pair<SpannableString, SpannableStringBuilder>>>) myUserResponse -> {
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
                    coolSubtitle.append(" ");
                    SpannableString coolTitle = new SpannableString(myUserResponse.getName() + "  " + myUserResponse.getLevel().getCurrent());
                    coolTitle.setSpan(new ForegroundColorSpan(Color.HSVToColor(getHSV(myUserResponse.getRating(), myUserResponse.getActivity()))), 0, name.length(), 0);
                    Database db = Room.databaseBuilder(context, Database.class, "achievements.db")
                            .fallbackToDestructiveMigration()
                            .createFromAsset("databases/achievements.db")
                            .build();
                    AchievementDao dao = db.getDao();
                    for (int i = 0; i < 3; i++) {
                        Achievement achievement1 = dao.getById(myUserResponse.getAchieves().get(i));
                        SpannableString text = new SpannableString(achievement1.getText());
                        text.setSpan(new RoundedBackgroundSpan(Color.parseColor(achievement1.getColor())), 0, text.length(), 0);
                        coolSubtitle.append(text);
                        coolSubtitle.append(" ");
                    }
                    Observable<Pair<SpannableString, SpannableStringBuilder>> strings = Observable
                            .create((ObservableOnSubscribe<Pair<SpannableString, SpannableStringBuilder>>) emitter -> {
                                if (!emitter.isDisposed()) {
                                    Pair<SpannableString, SpannableStringBuilder> titles = new Pair<>(coolTitle, coolSubtitle);
                                    emitter.onNext(titles);
                                    emitter.onComplete();
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                    return strings;
                })
                .subscribe(new Observer<Pair<SpannableString, SpannableStringBuilder>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(Pair<SpannableString, SpannableStringBuilder> titles) {
                        actionBar.setTitle(titles.first);
                        actionBar.setSubtitle(titles.second);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, "Неизвестная ошибка при обновление информации о пользователе", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return disposables;
    }

    //this method checks if we have new Cookies or have userData and decides which login method to perform
    public static CompositeDisposable setNiceTitle(androidx.appcompat.app.ActionBar actionBar, Context context, NavigationView navigationView) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.app_preferences), Context.MODE_PRIVATE);
        boolean noCookies = sharedPref.getBoolean(context.getString(R.string.noCookies), true);
        boolean oldCookies = sharedPref.getBoolean(context.getString(R.string.oldCookies), true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        if (!noCookies) {
            if (!oldCookies) {
                compositeDisposable = getMyUser(sharedPref.getString(context.getString(R.string.cookies), ""), actionBar, context);
            } else {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(context.getString(R.string.oldCookies), false);
                editor.apply();
                compositeDisposable = getCookiesAndMyUser(context, actionBar, new UserData(sharedPref.getString(context.getString(R.string.login), ""), sharedPref.getString(context.getString(R.string.password), "")), navigationView, false, null);
            }
        } else {
            actionBar.setTitle("Неизвестный пользователь");
        }
        return compositeDisposable;
    }
}