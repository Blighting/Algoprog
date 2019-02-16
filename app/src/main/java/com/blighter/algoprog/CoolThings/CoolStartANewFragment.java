package com.blighter.algoprog.CoolThings;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.blighter.algoprog.Fragments.ModuleFragment;
import com.blighter.algoprog.R;

public class CoolStartANewFragment {
    FragmentManager fragmentManager;
    ModuleFragment fragment;
    String id;

    public CoolStartANewFragment(FragmentManager coolFragmentManager, ModuleFragment coolFragment, String coolId) {
        id = coolId;
        fragment = coolFragment;
        fragmentManager = coolFragmentManager;
    }

    public void startFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container_in_Main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
