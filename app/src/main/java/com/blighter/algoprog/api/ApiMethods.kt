package com.blighter.algoprog.api

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.blighter.algoprog.R
import com.blighter.algoprog.network.BestSolution
import com.blighter.algoprog.network.BestSolutionsInterface
import com.blighter.algoprog.network.ClientCoroutines.client
import com.blighter.algoprog.network.Task
import com.blighter.algoprog.network.TaskInterface
import com.mukesh.MarkdownView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.nodes.Document

val scopeNetwork = CoroutineScope(Dispatchers.IO)
val scopeUI = CoroutineScope(Dispatchers.Main)
private fun replaceHtmlTags(s: String?): String? {
    return s?.replace("&lt;".toRegex(), "<")?.replace("&gt;".toRegex(), ">")?.replace("&le;".toRegex(), "<=")?.replace("&ge;".toRegex(), ">=")
}

fun setBestSoultions(cookies: String?, taskId: String?, context: Context) {
    var error = false
    var solutions: List<BestSolution?>? = null
    scopeNetwork.launch {
        try {
            solutions = client.create(BestSolutionsInterface::class.java).getBestSolutions(cookies, taskId).execute().body()
        } catch (e: Exception) {
            error = true
        }
    }
    scopeUI.launch {
        when {
            error -> {
                Toast.makeText(context, "Не удалось связаться с сервером. Проверьте подключение к интернету.", Toast.LENGTH_SHORT).show()
            }
            solutions == null -> {
                Toast.makeText(context, "Неизвестная ошибка", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val settings = Document.OutputSettings().prettyPrint(false)
                val markdownHtml = StringBuilder()
                //adding all information in one SpannableString
                for (i in solutions!!.indices) {
                    //replacing all additional information besides language name
                    markdownHtml.append("\n```").append(solutions!![i]!!.language.replace("[^a-zA-Z]+".toRegex(), "")).append("\n")
                    markdownHtml.append(replaceHtmlTags(solutions!![i]?.source)).append("\n```\n")
                    //replacing html tags so code looks clean
                    markdownHtml.append("<p>").append(solutions!![i]!!.language).append("</p>").append("\n<hr></hr>\n")
                }
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setPositiveButton("Закрыть") { dialog: DialogInterface?, which: Int -> }
                val mView = (context as FragmentActivity).layoutInflater.inflate(R.layout.alert_dialog_for_best_submits, null)
                val markdownView: MarkdownView = mView.findViewById(R.id.markdown_view)
                markdownView.setMarkDownText(markdownHtml.toString())
                alertDialog.setView(mView)
                val alertDialog1 = alertDialog.create()
                alertDialog1.show()
            }
        }
    }
}


fun setTask(id: String?, browser: WebView, context: Context?) {
    var error = false
    var task: Task? = null
    scopeNetwork.launch {
        try {
            task = client.create(TaskInterface::class.java).getTask(id).execute().body()
        } catch (e: Exception) {
            error = true
        }
        withContext(Dispatchers.Main) {
            when {
                error -> {
                    Toast.makeText(context, "Не удалось связаться с сервером. Проверьте подключение к интернету.", Toast.LENGTH_SHORT).show()
                }
                task == null -> {
                    Toast.makeText(context, "Неизвестная ошибка, возможно такой задачи не существует", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    //adding js code so we can implement MathJax into WebView
                    val styles = """<head>
      <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
      <link rel="stylesheet" href="/bootstrap.min.css">
    </head><script type="text/x-mathjax-config">  MathJax.Hub.Config({
      CommonHTML: { linebreaks: { automatic: true },EqnChunk:(MathJax.Hub.Browser.isMobile?10:50) },displayAlign: "left",
      "HTML-CSS": { linebreaks: { automatic: true } ,
        preferredFont: "STIX"},extensions: ["tex2jax.js"],messageStyle:"none",jax: ["input/TeX", "input/MathML","output/HTML-CSS"],tex2jax: {inlineMath: [['$','$'],['\\(','\\)']]}});</script><script type="text/javascript" async src="file:///android_asset/MathJax/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script></body></html>"""
                    val basicUrl = "https://algoprog.ru/material/$id"
                    //we replace all http with https to prevent mixed content error
                    val content = task?.content?.replace("http", "https")
                    browser.settings.javaScriptEnabled = true
                    browser.loadDataWithBaseURL(basicUrl, styles + content, "text/html", "utf-8", "")
                }
            }
        }
    }
}