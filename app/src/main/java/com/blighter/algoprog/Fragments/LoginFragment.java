package com.blighter.algoprog.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.blighter.algoprog.POJO.UserData;
import com.blighter.algoprog.R;

import static com.blighter.algoprog.API.ApiMethods.sendDataForCookies;

public class LoginFragment extends Fragment {
    public static final Boolean AUTHORIZED = false;
    public static final String PASSWORD = "password";
    public static final String LOGIN = "login";
    public static final String APP_PREFERENCES = "justSomeData";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        final Button button = (Button) view.findViewById(R.id.buttonForLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText loginText = getActivity().findViewById(R.id.login);
                EditText passwordText = getActivity().findViewById(R.id.password);
                if (loginText.getText().toString().equals(""))
                    Toast.makeText(getActivity(), "Введите ваш логин", Toast.LENGTH_SHORT).show();
                else if (passwordText.getText().toString().equals(""))
                    Toast.makeText(getActivity(), "Введите ваш пароль", Toast.LENGTH_SHORT).show();
                else {
                    UserData user = new UserData(loginText.getText().toString(), passwordText.getText().toString());
                    sendDataForCookies(user, getActivity(), ((AppCompatActivity) getActivity()).getSupportActionBar());
                    CheckBox checkBox = getActivity().findViewById(R.id.cb_for_saving_data);

                    if (checkBox.isChecked()) {
                        SharedPreferences data = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = data.edit();
                        editor.putString(LOGIN, loginText.getText().toString());
                        editor.putString(PASSWORD, passwordText.getText().toString());
                        editor.putBoolean("AUTHORIZED", true);
                        editor.apply();
                    }
                    addMenuItemInNavMenuDrawer();
                }
            }
        });
        return view;
    }

    private void addMenuItemInNavMenuDrawer() {
        NavigationView navView = (NavigationView) getActivity().findViewById(R.id.nav_viewInMain);

        Menu menu = navView.getMenu();
        SharedPreferences sharedPref = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
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


}