package com.blighter.algoprog.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.blighter.algoprog.R
import com.blighter.algoprog.api.Errors
import com.blighter.algoprog.api.scopeNetwork
import com.blighter.algoprog.utils.UrlInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

//this fragment shows any module from Algoprog
class ModuleFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_module, container, false)
        val url: String?
        val bundle = arguments
        //url check, that we got from intent and start setModule()
        if (bundle != null) {
            url = bundle.getString("url")
            setModule(requireContext(), url, view.findViewById(R.id.browser))
        } else {
            Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_LONG).show()
            requireActivity().onBackPressed()
        }
        return view
    }

    private fun setModule(context: Context, url: String?, browser: WebView) {
        scopeNetwork.launch {
            var doc: Document? = null
            var error = false
            try {
                doc = Jsoup.connect(url).get()
            } catch (e: Exception) {
                error = true
            }
            withContext(Dispatchers.Main) {
                when {
                    error -> {
                        Toast.makeText(context, Errors.NoInternet.warn, Toast.LENGTH_SHORT).show()
                        (context as FragmentActivity).onBackPressed()
                    }
                    doc == null -> {
                        Toast.makeText(context, Errors.Unknown.warn, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        //delete all useless html code
                        doc.getElementsByClass("navbar navbar-default navbar-fixed-top").remove()
                        doc.getElementsByClass("_client_components_Sceleton__footer").remove()
                        doc.getElementsByClass("breadcrumb").remove()
                        doc.getElementsByClass("_2ypNosdSxDScDQXykk62PR").remove()
                        //setting our custom WebViewClient ( class from utils)
                        browser.webViewClient = UrlInterceptor(context)
                        browser.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                        browser.settings.textZoom = 115
                        browser.loadDataWithBaseURL(url, doc.toString(), "text/html", "utf-8", "")
                    }
                }
            }
        }
    }
}