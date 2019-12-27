package com.blighter.algoprog.api;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.blighter.algoprog.R;
import com.blighter.algoprog.pojo.BestSolution;
import com.blighter.algoprog.pojo.Solution;
import com.blighter.algoprog.pojo.Task;
import com.blighter.algoprog.pojo.me;
import com.blighter.algoprog.retrofit.BestSolutionsInterface;
import com.blighter.algoprog.retrofit.Client;
import com.blighter.algoprog.retrofit.MeInterface;
import com.blighter.algoprog.retrofit.SolutionsInterface;
import com.blighter.algoprog.retrofit.TaskInterface;
import com.mukesh.MarkdownView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiMethods {

    public static CompositeDisposable getBestSolutions(String cookies, String taskId, Context context) {
        CompositeDisposable disposables = new CompositeDisposable();
        BestSolutionsInterface client = Client.getClient().create(BestSolutionsInterface.class);
        client.getBestSolutions(cookies, taskId).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BestSolution>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<BestSolution> bestSolutions) {
                        Document.OutputSettings settings = new Document.OutputSettings().prettyPrint(false);
                        StringBuilder markdownHtml = new StringBuilder();
                        for (int i = 0; i < bestSolutions.size(); i++) {
                            markdownHtml.append("\n```").append(bestSolutions.get(i).getLanguage().replaceAll("[^a-zA-Z]+", "")).append("\n");
                            markdownHtml.append(replaceHtmlTags(bestSolutions.get(i).getSource())).append("\n```\n");
                            markdownHtml.append("<p>").append(bestSolutions.get(i).getLanguage()).append("</p>").append("\n<hr></hr>\n");
                        }
                        String cool = Jsoup.clean(bestSolutions.get(1).getSource(), "", Whitelist.none(), settings);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setPositiveButton("Закрыть", (dialog, which) -> {
                        });
                        View mView = ((FragmentActivity) context).getLayoutInflater().inflate(R.layout.alert_dialog_for_best_submits, null);
                        MarkdownView markdownView = mView.findViewById(R.id.markdown_view);
                        markdownView.setMarkDownText(markdownHtml.toString());
                        alertDialog.setView(mView);
                        AlertDialog alertDialog1 = alertDialog.create();
                        alertDialog1.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, "Неизвестная ошибка", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return disposables;
    }

    // отправление запроса для получения класса Me
    public static void askForMe(String cookies, final Context context) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://algoprog.ru/api/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        MeInterface client = retrofit.create(MeInterface.class);
        Call<me> call = client.getMeInfo(cookies);
        call.enqueue(new Callback<me>() {
            @Override
            public void onResponse(Call<me> call, Response<me> response) {
            }

            @Override
            public void onFailure(Call<me> call, Throwable t) {
                Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void getSolutions(final String id, String COOKIES, String userId) {
        SolutionsInterface client = Client.getClient().create(SolutionsInterface.class);
        client.getSolutions(COOKIES, userId, id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Solution>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Solution> solutions) {
                        solutions.get(0).getResults();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public static CompositeDisposable setTask(final String id, final WebView browser, final Context context) {
        TaskInterface client = Client.getClient().create(TaskInterface.class);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        client.getTask(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Task>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Task task) {
                        String styles = "<head> \n" +
                                "  <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css\"> \n" +
                                "  <link rel=\"stylesheet\" href=\"/bundle.css\"> \n" +
                                "  <link rel=\"stylesheet\" href=\"/bootstrap.min.css\"> \n" +
                                "  <link rel=\"stylesheet\" href=\"/react-diff-view.css\"> \n" +
                                "  <link rel=\"stylesheet\" href=\"/informatics.css\"> \n" +
                                "  <link rel=\"stylesheet\" href=\"/highlight.css\"> \n" +
                                "  <link rel=\"stylesheet\" href=\"/main.css\"> \n" +
                                " <meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" />" +
                                "</head>" +
                                "<script type=\"text/x-mathjax-config\">" +
                                "  MathJax.Hub.Config({\n" +
                                "  CommonHTML: { linebreaks: { automatic: true },EqnChunk:(MathJax.Hub.Browser.isMobile?10:50) },displayAlign: \"left\",\n" +
                                "  \"HTML-CSS\": { linebreaks: { automatic: true } ," +
                                "\n" +
                                "    preferredFont: \"STIX\"}," +
                                "extensions: [\"tex2jax.js\"],messageStyle:\"none\"," +
                                "jax: [\"input/TeX\", \"input/MathML\",\"output/HTML-CSS\"]," +
                                "tex2jax: {inlineMath: [['$','$'],['\\\\(','\\\\)']]}" +
                                "});" +
                                "</script>" +
                                "<script type=\"text/javascript\" async src=\"file:///android_asset/MathJax/MathJax.js?config=TeX-AMS-MML_HTMLorMML\"></script>" +
                                "" +
                                "</body>" +
                                "</html>";
                        String basicUrl = "https://algoprog.ru/material/" + id;
                        String content = task.getContent();
                        browser.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                        browser.getSettings().setJavaScriptEnabled(true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            browser.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                        }
                        browser.loadDataWithBaseURL(basicUrl, styles + content, "text/html", "utf-8", "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, "Не удалось связаться с сервером. Проверьте подключение к интернету.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return compositeDisposable;


    }

    private static String replaceHtmlTags(String s) {
        return s.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&le;", "<=").replaceAll("&ge;", ">=");
    }
}
