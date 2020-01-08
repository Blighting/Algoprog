package com.blighter.algoprog.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blighter.algoprog.R;
import com.blighter.algoprog.fragments.LoginFragment;
import com.blighter.algoprog.fragments.ModuleFragment;
import com.blighter.algoprog.fragments.StarterFragment;
import com.blighter.algoprog.fragments.TaskFragment;
import com.blighter.algoprog.fragments.TaskListsFragment;
import com.blighter.algoprog.utils.CoolStartANewFragment;
import com.google.android.material.navigation.NavigationView;

import io.reactivex.disposables.CompositeDisposable;

import static com.blighter.algoprog.api.LoginMethods.setNiceTitle;
import static com.blighter.algoprog.api.MenuMethods.menuExit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private CompositeDisposable disposables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        NavigationView navigationView = findViewById(R.id.nav_viewInMain);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.app_bar_in_main);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        addMenuItemInNavMenuDrawer();
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Неизвестный Пользователь");
        disposables = setNiceTitle(ab, MainActivity.this, navigationView);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StarterFragment starterFragment = new StarterFragment();
        fragmentTransaction.replace(R.id.container_in_Main, starterFragment);
        fragmentTransaction.commit();
        //trying to get intents in case of using app to go through link with algoprog
        if (intent != null && intent.getData() != null) {
            Uri uri = intent.getData();
            String url = uri.toString();
            String id = url.replaceAll("https://algoprog.ru/material/", "");
            if (id.contains("p")) {
                Bundle bundle = new Bundle();
                bundle.putString("idForTask", id);
                TaskFragment taskFragment = new TaskFragment();
                taskFragment.setArguments(bundle);
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.container_in_Main, taskFragment);
                fragmentTransaction1.commit();
            } else if (url.length() == 34) {
                Bundle bundle = new Bundle();
                bundle.putString("idForTaskList", id);
                TaskListsFragment taskListFragment = new TaskListsFragment();
                taskListFragment.setArguments(bundle);
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.container_in_Main, taskListFragment);
                fragmentTransaction1.commit();
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                ModuleFragment moduleFragment = new ModuleFragment();
                moduleFragment.setArguments(bundle);
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.container_in_Main, moduleFragment);
                fragmentTransaction1.commit();
            }
        }
    }

    private void addMenuItemInNavMenuDrawer() {
        NavigationView navView = findViewById(R.id.nav_viewInMain);
        Menu menu = navView.getMenu();
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.app_preferences), Context.MODE_PRIVATE);
        boolean authorized = sharedPref.getBoolean("WEHAVECOOKIES", false);
        menu.removeItem(R.id.nav_enter);
        if (authorized) {
            menu.clear();
            navView.inflateMenu(R.menu.drawer_logged);
        } else {
            menu.clear();
            navView.inflateMenu(R.menu.drawer_login);
        }
        navView.invalidate();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.nav_faq:
                ModuleFragment moduleFragment = new ModuleFragment();
                CoolStartANewFragment coolStartANewFragment = new CoolStartANewFragment(getSupportFragmentManager(), moduleFragment, getResources().getString(R.string.about_course_url));
                coolStartANewFragment.startFragment();
                break;
            case R.id.nav_news:
                ModuleFragment moduleFragment0 = new ModuleFragment();
                CoolStartANewFragment coolStartANewFragment0 = new CoolStartANewFragment(getSupportFragmentManager(), moduleFragment0, getResources().getString(R.string.news_url));
                coolStartANewFragment0.startFragment();
                break;
            case R.id.nav_level_1:
                ModuleFragment moduleFragment1 = new ModuleFragment();
                CoolStartANewFragment coolStartANewFragment1 = new CoolStartANewFragment(getSupportFragmentManager(), moduleFragment1, getResources().getString(R.string.level_1_url));
                coolStartANewFragment1.startFragment();
                break;
            case R.id.nav_level_2:
                ModuleFragment moduleFragment2 = new ModuleFragment();
                CoolStartANewFragment coolStartANewFragment2 = new CoolStartANewFragment(getSupportFragmentManager(), moduleFragment2, getResources().getString(R.string.level_2_url));
                coolStartANewFragment2.startFragment();
                break;
            case R.id.nav_level_3:
                ModuleFragment moduleFragment3 = new ModuleFragment();
                CoolStartANewFragment coolStartANewFragment3 = new CoolStartANewFragment(getSupportFragmentManager(), moduleFragment3, getResources().getString(R.string.level_3_url));
                coolStartANewFragment3.startFragment();
                break;
            case R.id.nav_level_4:
                ModuleFragment moduleFragment4 = new ModuleFragment();
                CoolStartANewFragment coolStartANewFragment4 = new CoolStartANewFragment(getSupportFragmentManager(), moduleFragment4, getResources().getString(R.string.level_4_url));
                coolStartANewFragment4.startFragment();
                break;
            case R.id.nav_level_5:
                ModuleFragment moduleFragment5 = new ModuleFragment();
                CoolStartANewFragment coolStartANewFragment5 = new CoolStartANewFragment(getSupportFragmentManager(), moduleFragment5, getResources().getString(R.string.level_5_url));
                coolStartANewFragment5.startFragment();
                break;
            case R.id.nav_level_6:
                ModuleFragment moduleFragment6 = new ModuleFragment();
                CoolStartANewFragment coolStartANewFragment6 = new CoolStartANewFragment(getSupportFragmentManager(), moduleFragment6, getResources().getString(R.string.level_6_url));
                coolStartANewFragment6.startFragment();
                break;
            case R.id.nav_level_7:
                ModuleFragment moduleFragment7 = new ModuleFragment();
                CoolStartANewFragment coolStartANewFragment7 = new CoolStartANewFragment(getSupportFragmentManager(), moduleFragment7, getResources().getString(R.string.level_7_url));
                coolStartANewFragment7.startFragment();
                break;
            case R.id.nav_level_8:
                ModuleFragment moduleFragment8 = new ModuleFragment();
                CoolStartANewFragment coolStartANewFragment8 = new CoolStartANewFragment(getSupportFragmentManager(), moduleFragment8, getResources().getString(R.string.level_8_url));
                coolStartANewFragment8.startFragment();
                break;
            case R.id.nav_level_9:
                ModuleFragment moduleFragment9 = new ModuleFragment();
                CoolStartANewFragment coolStartANewFragment9 = new CoolStartANewFragment(getSupportFragmentManager(), moduleFragment9, getResources().getString(R.string.level_9_url));
                coolStartANewFragment9.startFragment();
                break;
            case R.id.nav_level_10:
                ModuleFragment moduleFragment10 = new ModuleFragment();
                CoolStartANewFragment coolStartANewFragment10 = new CoolStartANewFragment(getSupportFragmentManager(), moduleFragment10, getResources().getString(R.string.level_10_url));
                coolStartANewFragment10.startFragment();
                break;
            case R.id.nav_enter:
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                LoginFragment loginFragment = new LoginFragment();
                fragmentTransaction.replace(R.id.container_in_Main, loginFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.nav_change_user:
                menuExit(this, getSupportActionBar());
                addMenuItemInNavMenuDrawer();
                FragmentManager fragmentManagerAfterExit = getSupportFragmentManager();
                FragmentTransaction fragmentTransactionAfterExit = fragmentManagerAfterExit.beginTransaction();
                LoginFragment loginFragmentAfterExit = new LoginFragment();
                fragmentTransactionAfterExit.replace(R.id.container_in_Main, loginFragmentAfterExit);
                fragmentTransactionAfterExit.addToBackStack(null);
                fragmentTransactionAfterExit.commit();
                break;
            case R.id.nav_exit:
                menuExit(this, getSupportActionBar());
                addMenuItemInNavMenuDrawer();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestart() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_preferences), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.oldCookies), true);
        editor.apply();
        super.onRestart();
    }

    @Override
    protected void onStop() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_preferences), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.oldCookies), true);
        editor.apply();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (disposables != null) {
            disposables.dispose();
        }
        super.onDestroy();
    }
}
