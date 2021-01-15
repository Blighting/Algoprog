package com.blighter.algoprog.utils

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.blighter.algoprog.R
import com.blighter.algoprog.fragments.ModuleFragment

//class for generating boilerplate code for menu items(like levels)
class CoolStartANewFragment(private val fragmentManager: FragmentManager, private val fragment: ModuleFragment, private val url: String) {
    fun startFragment() {
        val fragmentTransaction = fragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putString("url", url)
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.container_in_Main, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}