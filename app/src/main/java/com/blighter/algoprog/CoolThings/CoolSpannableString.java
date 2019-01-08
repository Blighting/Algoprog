package com.blighter.algoprog.CoolThings;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.blighter.algoprog.Fragments.ModuleFragment;
import com.blighter.algoprog.R;

public class CoolSpannableString {
    private String id;
    private String text;
    private FragmentActivity activity;
    private ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            ModuleFragment moduleFragment = new ModuleFragment();
            moduleFragment.setArguments(bundle);
            android.support.v4.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_in_Main, moduleFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(activity.getResources().getColor(R.color.colorForTextWithLink));
            ds.setUnderlineText(false);
        }
    };

    public CoolSpannableString(String coolId, String coolText, FragmentActivity coolActivity) {
        id = coolId;
        text = coolText;
        activity = coolActivity;
    }

    public SpannableString getString() {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(clickableSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    ;

}

