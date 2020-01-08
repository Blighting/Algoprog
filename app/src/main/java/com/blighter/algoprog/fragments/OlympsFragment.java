package com.blighter.algoprog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blighter.algoprog.R;

public class OlympsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_olymps, container, false);
        TextView reg = view.findViewById(R.id.tw_olymp_reg);
        reg.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("url", "https://algoprog.ru/material/reg");
            ModuleFragment moduleFragment = new ModuleFragment();
            moduleFragment.setArguments(bundle);
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_in_Main, moduleFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        TextView roi = view.findViewById(R.id.tw_olymp_roi);
        roi.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("url", "https://algoprog.ru/material/roi");
            ModuleFragment moduleFragment = new ModuleFragment();
            moduleFragment.setArguments(bundle);
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_in_Main, moduleFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        return view;
    }
}
