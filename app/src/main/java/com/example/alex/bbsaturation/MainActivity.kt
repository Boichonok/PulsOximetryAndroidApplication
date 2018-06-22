package com.example.alex.bbsaturation

import android.content.Intent
import android.os.Bundle
import android.provider.Telephony
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.alex.bbsaturation.BBSaturation_DataBase.BBSaturation_DataBase
import com.example.alex.bbsaturation.BBSaturation_stream.BBSaturation_Desck_Top_Fragment
import com.example.alex.bbsaturation.BBSaturation_stream.BBSaturation_Stream_Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var bbSaturationStreamFragment : BBSaturation_Stream_Fragment? = null
    private var bbSaturationDeskTop : BBSaturation_Desck_Top_Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)



        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        initFragment()

    }

    private fun initFragment() {
        this.bbSaturationStreamFragment = BBSaturation_Stream_Fragment()
        this.bbSaturationDeskTop = BBSaturation_Desck_Top_Fragment()
        fragmentManager.beginTransaction().replace(R.id.frame_for_fragments,bbSaturationDeskTop).commit()

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

            R.id.nav_Stream -> {
                fragmentManager.beginTransaction().replace(R.id.frame_for_fragments,bbSaturationStreamFragment).commit()
            }
            R.id.desckTop -> {
               fragmentManager.beginTransaction().replace(R.id.frame_for_fragments,bbSaturationDeskTop).commit()
            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_chat -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onResume() {
        super.onResume()

    }
}
