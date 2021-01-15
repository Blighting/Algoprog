package com.blighter.algoprog.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.blighter.algoprog.R
import com.blighter.algoprog.fragments.ModuleFragment
import com.blighter.algoprog.fragments.TaskFragment
import com.blighter.algoprog.fragments.TaskListsFragment
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

//custom WebViewClient so new materials wont trigger Main Activity intent interceptor
class UrlInterceptor(var context: Context) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if (url.contains("https://algoprog.ru/material/")) {
            var id = url.replace("https://algoprog.ru/material/".toRegex(), "")
            when {
                (id.contains("p") && id.contains("^[0-9]*\$") && !id.contains(".")) -> {
                    val bundle = Bundle()
                    bundle.putString("id", id)
                    val taskFragment = TaskFragment()
                    taskFragment.arguments = bundle
                    val fragmentManager1 = (context as FragmentActivity).supportFragmentManager
                    val fragmentTransaction1 = fragmentManager1.beginTransaction()
                    fragmentTransaction1.replace(R.id.container_in_Main, taskFragment)
                    fragmentTransaction1.addToBackStack(null)
                    fragmentTransaction1.commit()
                }
                id.contains(".") -> {
                    try {
                        id = URLDecoder.decode(id, "UTF-8")
                        val bundle = Bundle()
                        bundle.putString("idForTaskList", id)
                        val taskListFragment = TaskListsFragment()
                        taskListFragment.arguments = bundle
                        val fragmentManager1 = (context as FragmentActivity).supportFragmentManager
                        val fragmentTransaction1 = fragmentManager1.beginTransaction()
                        fragmentTransaction1.replace(R.id.container_in_Main, taskListFragment)
                        fragmentTransaction1.addToBackStack(null)
                        fragmentTransaction1.commit()
                    } catch (e: UnsupportedEncodingException) {
                        Toast.makeText(context, "Произошла неизвестная ошибка", Toast.LENGTH_LONG).show()
                    }
                }
                else -> {
                    val bundle = Bundle()
                    bundle.putString("url", url)
                    val moduleFragment = ModuleFragment()
                    moduleFragment.arguments = bundle
                    val fragmentManager1 = (context as FragmentActivity).supportFragmentManager
                    val fragmentTransaction1 = fragmentManager1.beginTransaction()
                    fragmentTransaction1.replace(R.id.container_in_Main, moduleFragment)
                    fragmentTransaction1.addToBackStack(null)
                    fragmentTransaction1.commit()
                }
            }
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
            view.reload()
        }
        return super.shouldOverrideUrlLoading(view, url)
    }
}