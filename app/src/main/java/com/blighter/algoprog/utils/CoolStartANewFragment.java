package com.blighter.algoprog.utils;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.blighter.algoprog.R;
import com.blighter.algoprog.fragments.ModuleFragment;

public class CoolStartANewFragment {
    private final FragmentManager fragmentManager;
    private final ModuleFragment fragment;
    private final String url;

    public CoolStartANewFragment(FragmentManager coolFragmentManager, ModuleFragment coolFragment, String coolUrl) {
        url = coolUrl;
        fragment = coolFragment;
        fragmentManager = coolFragmentManager;
    }

    public void startFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container_in_Main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
