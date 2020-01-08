package com.blighter.algoprog.api;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
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

import org.jsoup.nodes.Document;

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

//This class contains some methods which interacts with Algoprog Api(or algoprog itself) but can be used in different fragments
public class ApiMethods {
    private static String replaceHtmlTags(String s) {
        return s.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&le;", "<=").replaceAll("&ge;", ">=");
    }

    //this method gets BestSolutions from Algoprog Api snd shows them in dialog with MarkDownView
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
                        //adding all information in one SpannableString
                        for (int i = 0; i < bestSolutions.size(); i++) {
                            //replacing all additional information besides language name
                            markdownHtml.append("\n```").append(bestSolutions.get(i).getLanguage().replaceAll("[^a-zA-Z]+", "")).append("\n");
                            markdownHtml.append(replaceHtmlTags(bestSolutions.get(i).getSource())).append("\n```\n");
                            //replacing html tags so code looks clean
                            markdownHtml.append("<p>").append(bestSolutions.get(i).getLanguage()).append("</p>").append("\n<hr></hr>\n");
                        }
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

    // getting Me from Algoprog Api
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

    // getting contest Solutions from Algoprog Api
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

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //Getting Task html from Algoprog with Jsoup, cleaning it and show it in WebView
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
                        //adding js code so we can implement MathJax into WebView
                        String styles = "<head> \n" +
                                "  <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css\"> \n" +
                                "  <link rel=\"stylesheet\" href=\"/bundle.css\"> \n" +
                                "  <link rel=\"stylesheet\" href=\"/bootstrap.min.css\"> \n" +
                                "  <link rel=\"stylesheet\" href=\"/react-diff-view.css\"> \n" +
                                "  <link rel=\"stylesheet\" href=\"/informatics.css\"> \n" +
                                "  <link rel=\"stylesheet\" href=\"/highlight.css\"> \n" +
                                "  <link rel=\"stylesheet\" href=\"/main.css\"> \n" +
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
                        //we replace all http with https to prevent mixed content error
                        String content = task.getContent().replace("http", "https");
                        browser.getSettings().setJavaScriptEnabled(true);
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

}
