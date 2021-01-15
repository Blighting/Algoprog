package com.blighter.algoprog.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.blighter.algoprog.R
import com.blighter.algoprog.api.setTask
import com.blighter.algoprog.utils.UrlInterceptor
import com.google.android.material.button.MaterialButton

//Fragment class which responsible for tasks
class TaskFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)
        var id: String? = null
        val bundle = arguments
        //getting Task id from bundle
        if (bundle != null) {
            id = bundle.getString("id")
        }
        val browser = view.findViewById<WebView>(R.id.wb_for_task)
        browser.webViewClient = UrlInterceptor(requireContext())
        browser.settings.textZoom = 110
        //setting everything in Task
        setTask(id, browser, context)
        //Creating MaterialButton with informatics string
        val link = "https://informatics.mccme.ru/moodle/mod/statements/view3.php?chapterid=" + id!!.replace("p", "")
        val button: MaterialButton = view.findViewById(R.id.btn_informatics)
        button.setOnClickListener { v: View? ->
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(link)
            startActivity(i)
        }
        return view
    }


}