package com.blighter.algoprog.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.blighter.algoprog.R
import com.blighter.algoprog.api.Errors
import com.blighter.algoprog.api.scopeNetwork
import com.blighter.algoprog.network.ClientCoroutines.client
import com.blighter.algoprog.network.MaterialsInTaskList
import com.blighter.algoprog.network.TaskListInterface
import com.blighter.algoprog.utils.UrlInterceptor
import com.google.android.material.card.MaterialCardView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//Fragment class which responsible for TaskList
class TaskListsFragment : Fragment() {
    private var filterAdapter = ListDelegationAdapter(taskAdapterDelegate())
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)
        var id: String? = null
        var url: String? = null
        val bundle = arguments
        if (bundle != null) {
            id = bundle.getString("idForTaskList")
            url = bundle.getString("urlForTaskList")
        }
        val textView = view.findViewById<TextView>(R.id.tw_task_list_title)
        getTasksList(id, requireContext(), textView, view.findViewById(R.id.browser_for_labels), url)
        return view
    }

    //getting contest tasks
    private fun getTasksList(id: String?, context: Context, title: TextView, labelsView: WebView, url: String?) {
        scopeNetwork.launch {
            var materialsInTaskList: MaterialsInTaskList? = null
            var error: String? = null
            try {
                materialsInTaskList = client.create(TaskListInterface::class.java).getTasks(id).execute().body()
            } catch (e: Exception) {
                error = Errors.NoInternet.warn
            }
            withContext(Dispatchers.Main) {
                when {
                    error != null -> {
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                    }
                    materialsInTaskList == null -> {
                        Toast.makeText(context, Errors.Unknown.warn, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        title.text = materialsInTaskList.title
                        val materials = materialsInTaskList.materials
                        val labels = StringBuilder()
                        val k = 0
                        while (materials[k].type == "label") {
                            labels.append(materials[k].content).append("<br><br>")
                        }
                        labelsView.settings.textZoom = 130
                        labelsView.webViewClient = UrlInterceptor(context)
                        labelsView.loadData(labels.toString(), "text/html", "utf-8")
                        val titles = ArrayList<String>()
                        val ids = ArrayList<String>()
                        //building arrays with task's titles and ids
                        for (i in k until materials.size) {
                            titles.add(i - k, materials[i].title)
                            ids.add(i - k, materials[i]._id)
                        }
                        val listViewTasks = (context as FragmentActivity?)!!.findViewById<ListView>(R.id.lv_for_tasks)
                        val taskAdapter = TaskAdapter(titles, context, 0)
                        listViewTasks.adapter = taskAdapter
                        listViewTasks.onItemClickListener = AdapterView.OnItemClickListener { _: AdapterView<*>?, _: View?, i: Int, l: Long ->
                            val bundle = Bundle()
                            bundle.putString("id", ids[i])
                            val taskFragment = TaskFragment()
                            taskFragment.arguments = bundle
                            val fragmentManager = (context as FragmentActivity?)!!.supportFragmentManager
                            val fragmentTransaction = fragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.container_in_Main, taskFragment)
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }

                    }
                }
            }
        }

    }

    //customAdapter for TaskListWithTasks
    private class TaskAdapter(var titles: ArrayList<String>, context: Context, resource: Int) : ArrayAdapter<String>(context, resource) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater = context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView = inflater.inflate(R.layout.list_item_task, parent, false)
            val textView = rowView.findViewById<View>(R.id.tw_list_item_task) as TextView
            textView.text = titles[position]
            return rowView
        }
    }

    private fun taskAdapterDelegate() = adapterDelegate<Task, Task>(R.layout.list_item_task) {
        val title: TextView = findViewById(R.id.tw_list_item_task)
        bind {
            title.text = item.title
        }
        val label: MaterialCardView = findViewById(R.id.task_item)
        label.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", item.id)
            val taskFragment = TaskFragment()
            taskFragment.arguments = bundle
            val fragmentManager = (context as FragmentActivity?)!!.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_in_Main, taskFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    private data class Task(val title: String, val id: String)
}
