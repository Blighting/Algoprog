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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.blighter.algoprog.R;
import com.blighter.algoprog.pojo.UserData;

import io.reactivex.disposables.CompositeDisposable;

import static com.blighter.algoprog.api.MustToUseMethods.getCookiesAndMyUser;

public class LoginFragment extends Fragment {
    public static final String SECOND_LEVEL_AUTHORIZED = "second_level_authorization";
    public static final String PASSWORD = "password";
    public static final String LOGIN = "login";
    public static final String APP_PREFERENCES = "justSomeData";
    private CompositeDisposable disposables;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        final Button button = (Button) view.findViewById(R.id.buttonForLogin);
        button.setOnClickListener(v -> {
            EditText loginText = getActivity().findViewById(R.id.login);
            EditText passwordText = getActivity().findViewById(R.id.password);
            if (loginText.getText().toString().equals(""))
                Toast.makeText(getActivity(), "Введите ваш логин", Toast.LENGTH_SHORT).show();
            else if (passwordText.getText().toString().equals(""))
                Toast.makeText(getActivity(), "Введите ваш пароль", Toast.LENGTH_SHORT).show();
            else {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                UserData user = new UserData(loginText.getText().toString(), passwordText.getText().toString());
                disposables = getCookiesAndMyUser(getContext(), ((AppCompatActivity) getActivity()).getSupportActionBar(), user, getActivity().findViewById(R.id.nav_viewInMain), true);
                CheckBox checkBox = getActivity().findViewById(R.id.cb_for_saving_data);
                if (checkBox.isChecked()) {
                    SharedPreferences data = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = data.edit();
                    editor.putString(LOGIN, loginText.getText().toString());
                    editor.putString(PASSWORD, passwordText.getText().toString());
                    editor.putBoolean(SECOND_LEVEL_AUTHORIZED, true);
                    editor.apply();
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        if (disposables != null) {
            disposables.dispose();
        }
        super.onDestroyView();
    }
}