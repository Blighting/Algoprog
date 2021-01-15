package com.blighter.algoprog.fragments

//import com.blighter.algoprog.api.LoginMethods
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.blighter.algoprog.R
import com.blighter.algoprog.api.setNiceTitle
import com.blighter.algoprog.network.UserData
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

//Fragment class which responsible for logging
class LoginFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val button = view.findViewById<Button>(R.id.btn_login)
        val passwordLayout: TextInputLayout = view.findViewById(R.id.txt_field_password)
        //the button is disabled by default because there are no text by default
        button.isEnabled = false
        button.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.primaryColor, null))
        val loginText: TextInputEditText = view.findViewById(R.id.et_login)
        val passwordText: TextInputEditText = view.findViewById(R.id.et_password)
        loginText.setText("mousedead")
        passwordText.setText("89374341400Q")
        button.isEnabled = true
        //if loginText or passwordText aren't empty set button enabled
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (loginText.text.toString().trim { it <= ' ' }.isNotEmpty() && passwordText.text.toString().trim { it <= ' ' }.isNotEmpty()) {
                    passwordLayout.helperText = null
                    button.isEnabled = true
                    button.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.secondaryColor, null))
                } else {
                    passwordLayout.helperText = null
                    button.isEnabled = false
                    button.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.primaryColor, null))
                }
            }

            override fun afterTextChanged(s: Editable) {}
        }
        loginText.addTextChangedListener(textWatcher)
        passwordText.addTextChangedListener(textWatcher)
        button.setOnClickListener { v: View? ->
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            val user = UserData(loginText.text.toString().trim(), passwordText.text.toString().trim())
            val data = requireActivity().getSharedPreferences(getString(R.string.app_preferences), Context.MODE_PRIVATE)
            //set noCookies to false because we got UserData, but we dont have cookies at all
            //so oldCookies are set to true
            val editor = data.edit()
            editor.putBoolean(getString(R.string.oldCookies), true)
            editor.putBoolean(getString(R.string.noCookies), false)
            editor.apply()
            setNiceTitle((activity as AppCompatActivity?)?.supportActionBar, requireContext(), requireActivity().findViewById(R.id.nav_viewInMain), true, user)
        }
        return view
    }
}