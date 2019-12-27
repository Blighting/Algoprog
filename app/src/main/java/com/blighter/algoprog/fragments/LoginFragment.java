package com.blighter.algoprog.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.blighter.algoprog.R;
import com.blighter.algoprog.pojo.UserData;

import io.reactivex.disposables.CompositeDisposable;

import static com.blighter.algoprog.api.MustToUseMethods.getCookiesAndMyUser;

public class LoginFragment extends Fragment {
    private CompositeDisposable disposables;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        final Button button = view.findViewById(R.id.buttonForLogin);
        button.setOnClickListener(v -> {
            EditText loginText = getActivity().findViewById(R.id.login);
            EditText passwordText = getActivity().findViewById(R.id.password);
            loginText.setText("mousedead");
            passwordText.setText("89374341400Q");
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
                    SharedPreferences data = getActivity().getSharedPreferences(getString(R.string.app_preferences), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = data.edit();
                    editor.putString(getString(R.string.login), loginText.getText().toString());
                    editor.putString(getString(R.string.password), passwordText.getText().toString());
                    editor.putBoolean(getString(R.string.first_level_authorization), true);
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