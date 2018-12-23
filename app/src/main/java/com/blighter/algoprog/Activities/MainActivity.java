package com.blighter.algoprog.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.blighter.algoprog.Fragments.AboutCourseFragment;
import com.blighter.algoprog.Fragments.LoginFragment;
import com.blighter.algoprog.Fragments.StarterFragment;
import com.blighter.algoprog.R;

import static com.blighter.algoprog.API.ApiMethods.COOKIES;
import static com.blighter.algoprog.API.MenuMethods.menuExit;
import static com.blighter.algoprog.API.MustToUseMethods.setNiceTitle;
import static com.blighter.algoprog.Fragments.LoginFragment.APP_PREFERENCES;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar_in_main);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        setNiceTitle(ab, MainActivity.this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StarterFragment starterFragment = new StarterFragment();
        fragmentTransaction.add(R.id.container_in_Main, starterFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        NavigationView navigationView = findViewById(R.id.nav_viewInMain);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        addMenuItemInNavMenuDrawer();
    }

    private void addMenuItemInNavMenuDrawer() {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_viewInMain);

        Menu menu = navView.getMenu();
        SharedPreferences sharedPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        Boolean authorized = sharedPref.getBoolean("WEHAVECOOKIES", false);
        menu.removeItem(R.id.nav_enter);
        if (authorized) {
            menu.add(R.id.settings_and_enter, R.id.nav_enter + 200, 2, R.string.change_user).setIcon(R.drawable.ic_menu_import_export_black);
            menu.add(R.id.settings_and_enter, R.id.nav_enter + 100, 2, R.string.exit).setIcon(R.drawable.ic_menu_export_black);
            menu.removeItem(R.id.nav_enter);
        } else {
            menu.removeItem(R.id.nav_enter + 200);
            menu.removeItem(R.id.nav_enter + 100);
            menu.add(R.id.settings_and_enter, R.id.nav_enter, 2, R.string.enter_in_Menu).setIcon(R.drawable.ic_menu_import_black);
        }

        navView.invalidate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(COOKIES);
        editor.putBoolean("WEHAVECOOKEIS", false);
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
            case R.id.nav_enter:
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                LoginFragment loginFragment = new LoginFragment();
                fragmentTransaction.replace(R.id.container_in_Main, loginFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.nav_faq:
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                AboutCourseFragment aboutCourseFragment = new AboutCourseFragment();
                fragmentTransaction1.replace(R.id.container_in_Main, aboutCourseFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
                break;
            case R.id.nav_enter + 200:
                menuExit(this, getSupportActionBar());
                addMenuItemInNavMenuDrawer();
                FragmentManager fragmentManagerAfterExit = getSupportFragmentManager();
                FragmentTransaction fragmentTransactionAfterExit = fragmentManagerAfterExit.beginTransaction();
                LoginFragment loginFragmentAfterExit = new LoginFragment();
                fragmentTransactionAfterExit.replace(R.id.container_in_Main, loginFragmentAfterExit);
                fragmentTransactionAfterExit.addToBackStack(null);
                fragmentTransactionAfterExit.commit();
                break;
            case R.id.nav_enter + 100:
                menuExit(this, getSupportActionBar());
                addMenuItemInNavMenuDrawer();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
