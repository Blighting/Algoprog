package com.blighter.algoprog.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.blighter.algoprog.R;
import com.blighter.algoprog.pojo.UserData;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import io.reactivex.disposables.CompositeDisposable;

import static com.blighter.algoprog.api.LoginMethods.getCookiesAndMyUser;

//Fragment class which responsible for logging
public class LoginFragment extends Fragment {
    private CompositeDisposable disposables;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        final Button button = view.findViewById(R.id.buttonForLogin);
        TextInputLayout passwordLayout = view.findViewById(R.id.passwordLayout);
        //the button is disabled by default because there are no text by default
        button.setEnabled(false);
        button.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.primaryColor, null));
        TextInputEditText loginText = view.findViewById(R.id.login);
        TextInputEditText passwordText = view.findViewById(R.id.password);
        //if loginText or passwordText aren't empty set button enabled
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((!loginText.getText().toString().trim().isEmpty()) && (!passwordText.getText().toString().trim().isEmpty())) {
                    passwordLayout.setError(null);
                    button.setEnabled(true);
                    button.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.secondaryColor, null));

                } else {
                    passwordLayout.setError(null);
                    button.setEnabled(false);
                    button.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.primaryColor, null));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        loginText.addTextChangedListener(textWatcher);
        passwordText.addTextChangedListener(textWatcher);
        button.setOnClickListener(v -> {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    UserData user = new UserData(loginText.getText().toString(), passwordText.getText().toString());
                    SharedPreferences data = getActivity().getSharedPreferences(getString(R.string.app_preferences), Context.MODE_PRIVATE);
                    //set noCookies to false because we got UserData, but we dont have cookies at all
                    //so oldCookies are set to true
                    SharedPreferences.Editor editor = data.edit();
                    editor.putBoolean(getString(R.string.oldCookies), true);
                    editor.putBoolean(getString(R.string.noCookies), false);
                    editor.apply();
                    disposables = getCookiesAndMyUser(getContext(), ((AppCompatActivity) getActivity()).getSupportActionBar(), user, getActivity().findViewById(R.id.nav_viewInMain), true, passwordLayout);
                }
        );
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