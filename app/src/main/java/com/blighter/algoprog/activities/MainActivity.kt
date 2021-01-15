package com.blighter.algoprog.activities

//import com.blighter.algoprog.api.LoginMethods
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.blighter.algoprog.R
import com.blighter.algoprog.api.MenuMethods
import com.blighter.algoprog.api.setNiceTitle
import com.blighter.algoprog.fragments.*
import com.blighter.algoprog.utils.CoolStartANewFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var mDrawerLayout: DrawerLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = intent
        val navigationView = findViewById<NavigationView>(R.id.nav_viewInMain)
        navigationView.setNavigationItemSelectedListener(this)
        mDrawerLayout = findViewById(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.app_bar_in_main)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        mDrawerLayout?.addDrawerListener(toggle)
        toggle.syncState()
        addMenuItemInNavMenuDrawer()
        val ab = supportActionBar
        ab!!.title = "Неизвестный Пользователь"
        setNiceTitle(ab, this@MainActivity, navigationView, false, null)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val starterFragment = StarterFragment()
        fragmentTransaction.replace(R.id.container_in_Main, starterFragment)
        fragmentTransaction.commit()
        //trying to get intents in case of using app to go through link with algoprog
        if (intent != null && intent.data != null) {
            val uri = intent.data
            val url = uri.toString()
            val id = url.replace("https://algoprog.ru/material/".toRegex(), "")
            when {
                id.contains("p") -> {
                    val bundle = Bundle()
                    bundle.putString("id", id)
                    val taskFragment = TaskFragment()
                    taskFragment.arguments = bundle
                    val fragmentManager1 = supportFragmentManager
                    val fragmentTransaction1 = fragmentManager1.beginTransaction()
                    fragmentTransaction1.replace(R.id.container_in_Main, taskFragment)
                    fragmentTransaction1.commit()
                }
                id.contains(".") -> {
                    val bundle = Bundle()
                    bundle.putString("idForTaskList", id)
                    val taskListFragment = TaskListsFragment()
                    taskListFragment.arguments = bundle
                    val fragmentManager1 = supportFragmentManager
                    val fragmentTransaction1 = fragmentManager1.beginTransaction()
                    fragmentTransaction1.replace(R.id.container_in_Main, taskListFragment)
                    fragmentTransaction1.commit()
                }
                else -> {
                    val bundle = Bundle()
                    bundle.putString("url", url)
                    val moduleFragment = ModuleFragment()
                    moduleFragment.arguments = bundle
                    val fragmentManager1 = supportFragmentManager
                    val fragmentTransaction1 = fragmentManager1.beginTransaction()
                    fragmentTransaction1.replace(R.id.container_in_Main, moduleFragment)
                    fragmentTransaction1.commit()
                }
            }
        }
    }

    private fun addMenuItemInNavMenuDrawer() {
        val navView = findViewById<NavigationView>(R.id.nav_viewInMain)
        val menu = navView.menu
        val sharedPref = getSharedPreferences(getString(R.string.app_preferences), MODE_PRIVATE)
        val authorized = sharedPref.getBoolean("WEHAVECOOKIES", false)
        menu.removeItem(R.id.nav_enter)
        if (authorized) {
            menu.clear()
            navView.inflateMenu(R.menu.drawer_logged)
        } else {
            menu.clear()
            navView.inflateMenu(R.menu.drawer_login)
        }
        navView.invalidate()
    }

    override fun onBackPressed() {
        if (mDrawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout!!.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_faq -> {
                val moduleFragment = ModuleFragment()
                val coolStartANewFragment = CoolStartANewFragment(supportFragmentManager, moduleFragment, resources.getString(R.string.about_course_url))
                coolStartANewFragment.startFragment()
            }
            R.id.nav_news -> {
                val moduleFragment0 = ModuleFragment()
                val coolStartANewFragment0 = CoolStartANewFragment(supportFragmentManager, moduleFragment0, resources.getString(R.string.news_url))
                coolStartANewFragment0.startFragment()
            }
            R.id.nav_level_1 -> {
                val moduleFragment1 = ModuleFragment()
                val coolStartANewFragment1 = CoolStartANewFragment(supportFragmentManager, moduleFragment1, resources.getString(R.string.level_1_url))
                coolStartANewFragment1.startFragment()
            }
            R.id.nav_level_2 -> {
                val moduleFragment2 = ModuleFragment()
                val coolStartANewFragment2 = CoolStartANewFragment(supportFragmentManager, moduleFragment2, resources.getString(R.string.level_2_url))
                coolStartANewFragment2.startFragment()
            }
            R.id.nav_level_3 -> {
                val moduleFragment3 = ModuleFragment()
                val coolStartANewFragment3 = CoolStartANewFragment(supportFragmentManager, moduleFragment3, resources.getString(R.string.level_3_url))
                coolStartANewFragment3.startFragment()
            }
            R.id.nav_level_4 -> {
                val moduleFragment4 = ModuleFragment()
                val coolStartANewFragment4 = CoolStartANewFragment(supportFragmentManager, moduleFragment4, resources.getString(R.string.level_4_url))
                coolStartANewFragment4.startFragment()
            }
            R.id.nav_level_5 -> {
                val moduleFragment5 = ModuleFragment()
                val coolStartANewFragment5 = CoolStartANewFragment(supportFragmentManager, moduleFragment5, resources.getString(R.string.level_5_url))
                coolStartANewFragment5.startFragment()
            }
            R.id.nav_level_6 -> {
                val moduleFragment6 = ModuleFragment()
                val coolStartANewFragment6 = CoolStartANewFragment(supportFragmentManager, moduleFragment6, resources.getString(R.string.level_6_url))
                coolStartANewFragment6.startFragment()
            }
            R.id.nav_level_7 -> {
                val moduleFragment7 = ModuleFragment()
                val coolStartANewFragment7 = CoolStartANewFragment(supportFragmentManager, moduleFragment7, resources.getString(R.string.level_7_url))
                coolStartANewFragment7.startFragment()
            }
            R.id.nav_level_8 -> {
                val moduleFragment8 = ModuleFragment()
                val coolStartANewFragment8 = CoolStartANewFragment(supportFragmentManager, moduleFragment8, resources.getString(R.string.level_8_url))
                coolStartANewFragment8.startFragment()
            }
            R.id.nav_level_9 -> {
                val moduleFragment9 = ModuleFragment()
                val coolStartANewFragment9 = CoolStartANewFragment(supportFragmentManager, moduleFragment9, resources.getString(R.string.level_9_url))
                coolStartANewFragment9.startFragment()
            }
            R.id.nav_level_10 -> {
                val moduleFragment10 = ModuleFragment()
                val coolStartANewFragment10 = CoolStartANewFragment(supportFragmentManager, moduleFragment10, resources.getString(R.string.level_10_url))
                coolStartANewFragment10.startFragment()
            }
            R.id.nav_olymps -> {
                val fragmentManager1 = supportFragmentManager
                val fragmentTransaction1 = fragmentManager1.beginTransaction()
                val olympsFragment = OlympsFragment()
                fragmentTransaction1.replace(R.id.container_in_Main, olympsFragment)
                fragmentTransaction1.addToBackStack(null)
                fragmentTransaction1.commit()
            }
            R.id.nav_enter -> {
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                val loginFragment = LoginFragment()
                fragmentTransaction.replace(R.id.container_in_Main, loginFragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            R.id.nav_change_user -> {
                MenuMethods.menuExit(this, supportActionBar)
                addMenuItemInNavMenuDrawer()
                val fragmentManagerAfterExit = supportFragmentManager
                val fragmentTransactionAfterExit = fragmentManagerAfterExit.beginTransaction()
                val loginFragmentAfterExit = LoginFragment()
                fragmentTransactionAfterExit.replace(R.id.container_in_Main, loginFragmentAfterExit)
                fragmentTransactionAfterExit.addToBackStack(null)
                fragmentTransactionAfterExit.commit()
            }
            R.id.nav_exit -> {
                MenuMethods.menuExit(this, supportActionBar)
                addMenuItemInNavMenuDrawer()
            }
        }
        mDrawerLayout!!.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onRestart() {
        val sharedPreferences = getSharedPreferences(getString(R.string.app_preferences), MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(getString(R.string.oldCookies), true)
        editor.apply()
        super.onRestart()
    }

    override fun onStop() {
        val sharedPreferences = getSharedPreferences(getString(R.string.app_preferences), MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(getString(R.string.oldCookies), true)
        editor.apply()
        super.onStop()
    }

}
