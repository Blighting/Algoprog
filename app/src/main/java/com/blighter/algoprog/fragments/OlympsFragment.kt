package com.blighter.algoprog.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.blighter.algoprog.R

class OlympsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_olymps, container, false)
        val reg = view.findViewById<TextView>(R.id.tw_olymp_reg)
        reg.setOnClickListener { v: View? ->
            val bundle = Bundle()
            bundle.putString("url", "https://algoprog.ru/material/reg")
            val moduleFragment = ModuleFragment()
            moduleFragment.arguments = bundle
            val fragmentManager = (context as FragmentActivity?)!!.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_in_Main, moduleFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        val roi = view.findViewById<TextView>(R.id.tw_olymp_roi)
        roi.setOnClickListener { v: View? ->
            val bundle = Bundle()
            bundle.putString("url", "https://algoprog.ru/material/roi")
            val moduleFragment = ModuleFragment()
            moduleFragment.arguments = bundle
            val fragmentManager = (context as FragmentActivity?)!!.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_in_Main, moduleFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        return view
    }
}