package com.blighter.algoprog.fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.blighter.algoprog.R
import com.blighter.algoprog.utils.CoolSpannableString

//just a fragment that sets an preset FAQ about Algoprog
class StarterFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_starting_menu, container, false)
        val forPupils = view.findViewById<TextView>(R.id.tv_hello_i_am_pupil)
        forPupils.movementMethod = LinkMovementMethod.getInstance()
        val howToStart = view.findViewById<TextView>(R.id.tv_how_to_start)
        howToStart.movementMethod = LinkMovementMethod.getInstance()
        val faq = view.findViewById<TextView>(R.id.tv_links)
        faq.movementMethod = LinkMovementMethod.getInstance()
        val coolSpannableStringForFullFaq = CoolSpannableString("https://algoprog.ru/material/0", getString(R.string.full_faq), requireActivity())
        val coolSpannableStringForPupilsFaq = CoolSpannableString("https://algoprog.ru/material/module-20927_5", getString(R.string.full_faq_for_pupils), requireActivity())
        faq.text = TextUtils.concat(coolSpannableStringForFullFaq.string, "| ", coolSpannableStringForPupilsFaq.string)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                val url = "https://algoprog.ru/register"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = resources.getColor(R.color.colorForTextWithLink)
                ds.isUnderlineText = false
            }
        }
        val coolSpannableStringForReg = SpannableString(getString(R.string.req))
        coolSpannableStringForReg.setSpan(clickableSpan, 0, getString(R.string.req).length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val coolSpannableStringForAboutCourse = CoolSpannableString("https://algoprog.ru/material/0", getString(R.string.about_course), requireActivity())
        howToStart.text = TextUtils.concat(coolSpannableStringForReg, " ", getString(R.string.our_bible), " ", coolSpannableStringForAboutCourse.string, ").")
        val coolSpannableStringForPayment = CoolSpannableString("https://algoprog.ru/material/pay", getString(R.string.payment), requireActivity())
        val payment = coolSpannableStringForPayment.string
        val hiIAmPupil = view.findViewById<Button>(R.id.btn_i_am_pupil)
        val hiIAmStudent = view.findViewById<Button>(R.id.btn_i_am_student)
        hiIAmPupil.setOnClickListener { v: View? ->
            hiIAmPupil.setBackgroundColor(Color.RED)
            hiIAmPupil.setTextColor(Color.WHITE)
            hiIAmStudent.setBackgroundColor(Color.WHITE)
            hiIAmStudent.setTextColor(resources.getColor(R.color.colorForMainText))
            forPupils.text = TextUtils.concat(getString(R.string.hello_i_am_pupil), payment, ".")
        }
        hiIAmStudent.setOnClickListener { v: View? ->
            hiIAmStudent.setBackgroundColor(Color.RED)
            hiIAmStudent.setTextColor(Color.WHITE)
            hiIAmPupil.setBackgroundColor(Color.WHITE)
            hiIAmPupil.setTextColor(resources.getColor(R.color.colorForMainText))
            forPupils.text = TextUtils.concat(getString(R.string.hello_i_am_student), payment, ".")
        }
        hiIAmPupil.performClick()
        return view
    }
}