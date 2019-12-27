package com.blighter.algoprog.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.blighter.algoprog.R;
import com.blighter.algoprog.fragments.ModuleFragment;

public class CoolSpannableString {
    private final String id;
    private final String text;
    private final FragmentActivity activity;
    private final ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View view) {
            ModuleFragment moduleFragment = new ModuleFragment();
            CoolStartANewFragment coolStartANewFragment = new CoolStartANewFragment(activity.getSupportFragmentManager(), moduleFragment, id);
            coolStartANewFragment.startFragment();
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

}

