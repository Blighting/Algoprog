package com.blighter.algoprog.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blighter.algoprog.CoolThings.CoolSpannableString;
import com.blighter.algoprog.R;

public class StarterFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_starting_menu, container, false);
        final CharSequence[] ssForLinks = {new CharSequence() {
            @Override
            public int length() {
                return 0;
            }

            @Override
            public char charAt(int i) {
                return 0;
            }

            @Override
            public CharSequence subSequence(int i, int i1) {
                return null;
            }

            @NonNull
            @Override
            public String toString() {
                return null;
            }
        }};
        final CharSequence[] ssForFullFaq = {new CharSequence() {
            @Override
            public int length() {
                return 0;
            }

            @Override
            public char charAt(int i) {
                return 0;
            }

            @Override
            public CharSequence subSequence(int i, int i1) {
                return null;
            }
        }};
        final CharSequence[] ssForPupils = {new CharSequence() {
            @Override
            public int length() {
                return 0;
            }

            @Override
            public char charAt(int i) {
                return 0;
            }

            @Override
            public CharSequence subSequence(int i, int i1) {
                return null;
            }
        }};
        final CharSequence[] ssForReqAndCourse = {new CharSequence() {
            @Override
            public int length() {
                return 0;
            }

            @Override
            public char charAt(int i) {
                return 0;
            }

            @Override
            public CharSequence subSequence(int i, int i1) {
                return null;
            }
        }};
        final TextView forPupils = (TextView) view.findViewById(R.id.tv_hello_i_am_pupil);
        forPupils.setMovementMethod(LinkMovementMethod.getInstance());
        final TextView howToStart = (TextView) view.findViewById(R.id.how_to_start);
        howToStart.setMovementMethod(LinkMovementMethod.getInstance());
        TextView faq = (TextView) view.findViewById(R.id.tv_links);
        faq.setMovementMethod(LinkMovementMethod.getInstance());
        CoolSpannableString coolSpannableStringForFullFaq = new CoolSpannableString("0", getString(R.string.full_faq), getActivity());
        CoolSpannableString coolSpannableStringForPupilsFaq = new CoolSpannableString("module-20927_5", getString(R.string.full_faq_for_pupils), getActivity());
        ssForLinks[0] = TextUtils.concat(coolSpannableStringForFullFaq.getString(), "| ", coolSpannableStringForPupilsFaq.getString());
        faq.setText(ssForLinks[0]);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                String url = "https://algoprog.ru/register";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.colorForTextWithLink));
                ds.setUnderlineText(false);
            }
        };
        SpannableString coolSpannableStringForReg = new SpannableString(getString(R.string.req));
        coolSpannableStringForReg.setSpan(clickableSpan,0,getString(R.string.req).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        CoolSpannableString coolSpannableStringForAboutCourse = new CoolSpannableString("0",getString(R.string.about_course),getActivity());
        ssForReqAndCourse[0] = TextUtils.concat(coolSpannableStringForReg," ",getString(R.string.our_bible)," ", coolSpannableStringForAboutCourse.getString(),").");
        howToStart.setText(ssForReqAndCourse[0]);
        CoolSpannableString coolSpannableStringForPayment = new CoolSpannableString("pay", getString(R.string.payment), getActivity());
        final SpannableString payment = coolSpannableStringForPayment.getString();
        final Button hiIAmPupil = (Button) view.findViewById(R.id.b_i_am_pupil);
        final Button hiIAmStudent = (Button) view.findViewById(R.id.b_i_am_student);
        hiIAmPupil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiIAmPupil.setBackgroundColor(Color.RED);
                hiIAmPupil.setTextColor(Color.WHITE);
                hiIAmStudent.setBackgroundColor(Color.WHITE);
                hiIAmStudent.setTextColor(getResources().getColor(R.color.colorForMainText));
                ssForFullFaq[0] = TextUtils.concat(getString(R.string.hello_i_am_pupil), payment, ".");
                forPupils.setText(ssForFullFaq[0]);
            }
        });
        hiIAmStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiIAmStudent.setBackgroundColor(Color.RED);
                hiIAmStudent.setTextColor(Color.WHITE);
                hiIAmPupil.setBackgroundColor(Color.WHITE);
                hiIAmPupil.setTextColor(getResources().getColor(R.color.colorForMainText));
                ssForPupils[0] = TextUtils.concat(getString(R.string.hello_i_am_student), payment, ".");
                forPupils.setText(ssForPupils[0]);
            }
        });
        hiIAmPupil.performClick();
        return view;
    }
}
