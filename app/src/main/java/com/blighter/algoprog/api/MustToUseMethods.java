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

import androidx.fragment.app.FragmentActivity;
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

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MustToUseMethods {
    private static final String PARTICIPANT_COLOR = "#997799";
    private static final String BRONZE_COLOR = "#cd7f32";
    private static final String SILVER_COLOR = "#b0b7c6";
    private static final String GOLD_COLOR = "#ffd700";

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

    public static CompositeDisposable getCookiesAndMyUser(Context context, final androidx.appcompat.app.ActionBar actionBar, UserData userData, NavigationView navView, Boolean firstTimeLoggingIn) {
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
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .switchMap(new Function<myUser, Observable<Pair<SpannableString, SpannableStringBuilder>>>() {
                    @Override
                    public Observable<Pair<SpannableString, SpannableStringBuilder>> apply(myUser myUserResponse) throws Exception {
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
                        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_preferences), Context.MODE_PRIVATE);
                        if (sharedPreferences.contains(context.getString(R.string.user_id))) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(context.getString(R.string.user_id), myUserResponse.get_id()).apply();
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
                    }
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
                        if (cookiesOnSuccess[0]) {
                            Log.d("тест", e.getCause().toString());
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

    private static CompositeDisposable getMyUser(String cookies, final androidx.appcompat.app.ActionBar actionBar, Context context) {
        CompositeDisposable disposables = new CompositeDisposable();
        MyUserInterface apiForMyUser = Client.getClient().create(MyUserInterface.class);
        apiForMyUser.getMyUserInfo(cookies)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .switchMap(new Function<myUser, Observable<Pair<SpannableString, SpannableStringBuilder>>>() {
                    @Override
                    public Observable<Pair<SpannableString, SpannableStringBuilder>> apply(myUser myUserResponse) throws Exception {
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
                        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_preferences), Context.MODE_PRIVATE);
                        if (sharedPreferences.contains(context.getString(R.string.user_id))) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(context.getString(R.string.user_id), myUserResponse.get_id()).apply();
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
                    }
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
                        ((FragmentActivity) context).onBackPressed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return disposables;
    }

    public static CompositeDisposable setNiceTitle(androidx.appcompat.app.ActionBar actionBar, Context context, NavigationView navigationView) {
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
