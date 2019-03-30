package com.blighter.algoprog.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.blighter.algoprog.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ModuleFragment extends Fragment {
    String url;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_module, container, false);
        String id = null;
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getString("id");
        }
        myAsyncTask showModule = new myAsyncTask(view, id);
        showModule.execute();
        return view;
    }

    private static class myAsyncTask extends AsyncTask<Void, Void, Document> {
        String basicUrl = "https://algoprog.ru/material/";
        View cont;
        Boolean allIsOk = true;

        myAsyncTask(View context, String id) {
            cont = context;
            basicUrl = basicUrl + id;
        }

        @Override
        protected Document doInBackground(Void... voids) {
            Document doc = null;
            try {
                doc = Jsoup.connect(basicUrl).get();
                doc.getElementsByClass("navbar navbar-default navbar-fixed-top").remove();
                doc.getElementsByClass("_client_components_Sceleton__footer").remove();
                doc.getElementsByClass("breadcrumb").remove();
            } catch (IOException e) {
                allIsOk = false;
            }

            return doc;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            if (allIsOk) {
                final WebView browser = cont.findViewById(R.id.browser);
                browser.loadDataWithBaseURL(basicUrl, document.toString(), "text/html", "utf-8", "");
                browser.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            } else
                Toast.makeText(cont.getContext(), "Нет подключения к интернету", Toast.LENGTH_SHORT).show();
        }
    }
}
