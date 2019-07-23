package com.blighter.algoprog.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.blighter.algoprog.R;
import com.blighter.algoprog.utils.UrlInterceptor;

import io.reactivex.disposables.CompositeDisposable;

import static com.blighter.algoprog.api.ApiMethods.setTask;

public class TaskFragment extends Fragment {
    private CompositeDisposable disposables;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        String id = null;
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getString("idForTask");
        }
        WebView browser = (WebView) view.findViewById(R.id.wb_for_task);
        browser.setWebViewClient(new UrlInterceptor(getContext()));
        CompositeDisposable compositeDisposable = setTask(id, browser, getContext());
        TextView tv_informatics = (TextView) view.findViewById(R.id.tv_in_task_informatics);
        final String link = "https://informatics.mccme.ru/moodle/mod/statements/view3.php?chapterid=" + id.replace("p", "");
        tv_informatics.setMovementMethod(LinkMovementMethod.getInstance());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                String url = link;
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
        SpannableString coolSpannableStringForInformatics = new SpannableString(getString(R.string.infromatics));
        coolSpannableStringForInformatics.setSpan(clickableSpan, 0, getString(R.string.infromatics).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_informatics.setText(coolSpannableStringForInformatics);
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
