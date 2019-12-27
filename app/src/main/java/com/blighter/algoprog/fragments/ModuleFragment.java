package com.blighter.algoprog.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.blighter.algoprog.R;
import com.blighter.algoprog.utils.UrlInterceptor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ModuleFragment extends Fragment {
    CompositeDisposable disposables;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_module, container, false);
        String url;
        Bundle bundle = getArguments();
        if (bundle != null) {
            url = bundle.getString("url");
            setModule(getContext(), url, view.findViewById(R.id.browser));
        } else {
            Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
            getActivity().onBackPressed();
        }
        return view;
    }

    private CompositeDisposable setModule(Context context, String url, WebView browser) {
        CompositeDisposable disposables = new CompositeDisposable();
        Observable.fromCallable(() -> Jsoup.connect(url).get()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Document>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(Document doc) {
                        doc.getElementsByClass("navbar navbar-default navbar-fixed-top").remove();
                        doc.getElementsByClass("_client_components_Sceleton__footer").remove();
                        doc.getElementsByClass("breadcrumb").remove();
                        browser.setWebViewClient(new UrlInterceptor(context));
                        browser.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                        browser.loadDataWithBaseURL(url, doc.toString(), "text/html", "utf-8", "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.getCause();
                        Toast.makeText(context, "Нет подключения к интернету", Toast.LENGTH_SHORT).show();
                        ((FragmentActivity) context).onBackPressed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return disposables;
    }

    @Override
    public void onDestroyView() {
        if (disposables != null) {
            disposables.dispose();
        }
        super.onDestroyView();
    }
}
