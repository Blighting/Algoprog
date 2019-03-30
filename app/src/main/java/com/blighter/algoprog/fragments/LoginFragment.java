package com.blighter.algoprog.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.blighter.algoprog.pojo.UserData;
import com.blighter.algoprog.R;

import static com.blighter.algoprog.api.ApiMethods.sendDataForCookies;

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
                    sendDataForCookies(user, getActivity(), ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity());
                    CheckBox checkBox = getActivity().findViewById(R.id.cb_for_saving_data);

                    if (checkBox.isChecked()) {
                        SharedPreferences data = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = data.edit();
                        editor.putString(LOGIN, loginText.getText().toString());
                        editor.putString(PASSWORD, passwordText.getText().toString());
                        editor.putBoolean("AUTHORIZED", true);
                        editor.apply();
                    }
                }
            }
        });
        return view;
    }
}