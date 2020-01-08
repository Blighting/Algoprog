package com.blighter.algoprog.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blighter.algoprog.R;
import com.blighter.algoprog.fragments.ModuleFragment;
import com.blighter.algoprog.fragments.TaskFragment;
import com.blighter.algoprog.fragments.TaskListsFragment;

//custom WebViewClient so new materials wont trigger Main Activity intent interceptor
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
                bundle.putString("id", id);
                TaskFragment taskFragment = new TaskFragment();
                taskFragment.setArguments(bundle);
                FragmentManager fragmentManager1 = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.container_in_Main, taskFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
            } else if (url.length() == 34) {
                Bundle bundle = new Bundle();
                bundle.putString("idForTaskList", id);
                TaskListsFragment taskListFragment = new TaskListsFragment();
                taskListFragment.setArguments(bundle);
                FragmentManager fragmentManager1 = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.container_in_Main, taskListFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                ModuleFragment moduleFragment = new ModuleFragment();
                moduleFragment.setArguments(bundle);
                FragmentManager fragmentManager1 = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.container_in_Main, moduleFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
            view.reload();
        }
        return super.shouldOverrideUrlLoading(view, url);
    }
}
