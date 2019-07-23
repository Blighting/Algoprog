package com.blighter.algoprog.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blighter.algoprog.R;
import com.blighter.algoprog.fragments.ModuleFragment;
import com.blighter.algoprog.fragments.TaskFragment;
import com.blighter.algoprog.fragments.TaskListsFragment;

public class UrlInterceptor extends WebViewClient {
    Context context;

    public UrlInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.contains("https://algoprog.ru/material/")) {
            String id = url.replaceAll("https://algoprog.ru/material/", "");
            if (id.contains("p")) {
                Bundle bundle = new Bundle();
                bundle.putString("idForTask", id);
                TaskFragment taskFragment = new TaskFragment();
                taskFragment.setArguments(bundle);
                android.support.v4.app.FragmentManager fragmentManager1 = ((FragmentActivity) context).getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.container_in_Main, taskFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
            } else if (url.length() == 34) {
                Bundle bundle = new Bundle();
                bundle.putString("idForTaskList", id);
                TaskListsFragment taskListFragment = new TaskListsFragment();
                taskListFragment.setArguments(bundle);
                android.support.v4.app.FragmentManager fragmentManager1 = ((FragmentActivity) context).getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.container_in_Main, taskListFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                ModuleFragment moduleFragment = new ModuleFragment();
                moduleFragment.setArguments(bundle);
                android.support.v4.app.FragmentManager fragmentManager1 = ((FragmentActivity) context).getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.container_in_Main, moduleFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            ((FragmentActivity) context).startActivity(intent);
            view.reload();
        }
        return super.shouldOverrideUrlLoading(view, url);
    }
}
