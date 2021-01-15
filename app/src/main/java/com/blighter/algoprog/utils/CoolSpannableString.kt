package com.blighter.algoprog.utils

import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.blighter.algoprog.R
import com.blighter.algoprog.fragments.ModuleFragment

//Class for SpannableString in startingFragment to write less boilerplate code
class CoolSpannableString(private val id: String, private val text: String, private val activity: FragmentActivity) {
    private val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(view: View) {
            val moduleFragment = ModuleFragment()
            val coolStartANewFragment = CoolStartANewFragment(activity.supportFragmentManager, moduleFragment, id)
            coolStartANewFragment.startFragment()
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = ContextCompat.getColor(activity, R.color.colorForTextWithLink)
            ds.isUnderlineText = false
        }
    }
    val string: SpannableString
        get() {
            val spannableString = SpannableString(text)
            spannableString.setSpan(clickableSpan, 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return spannableString
        }
}