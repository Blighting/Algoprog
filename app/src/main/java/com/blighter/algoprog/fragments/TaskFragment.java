package com.blighter.algoprog.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blighter.algoprog.R;
import com.blighter.algoprog.utils.UrlInterceptor;
import com.google.android.material.button.MaterialButton;

import io.reactivex.disposables.CompositeDisposable;

import static com.blighter.algoprog.api.ApiMethods.setTask;

//Fragment class which responsible for tasks
public class TaskFragment extends Fragment {
    private CompositeDisposable disposables;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        String id = null;
        Bundle bundle = getArguments();
        //getting Task id from bundle
        if (bundle != null) {
            id = bundle.getString("idForTask");
        }
        WebView browser = view.findViewById(R.id.wb_for_task);
        browser.setWebViewClient(new UrlInterceptor(getContext()));
        //setting everything in Task
        CompositeDisposable compositeDisposable = setTask(id, browser, getContext());
        //Creating MaterialButton with informatics string
        final String link = "https://informatics.mccme.ru/moodle/mod/statements/view3.php?chapterid=" + id.replace("p", "");
        MaterialButton button = view.findViewById(R.id.button_informatics);
        button.setOnClickListener(v -> {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(link));
                    startActivity(i);
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
