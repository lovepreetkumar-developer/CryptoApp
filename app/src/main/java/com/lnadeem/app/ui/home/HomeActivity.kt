package com.lnadeem.app.ui.home

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.lnadeem.app.R
import com.lnadeem.app.data.preferences.PreferenceProvider
import com.lnadeem.app.databinding.ActivityHomeBinding
import com.lnadeem.app.util.ScreenSlidePagerAdapter
import com.lnadeem.app.util.SoundService
import com.google.android.material.navigation.NavigationBarView
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*

class HomeActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener, KodeinAware {

    override val kodein by kodein()

    private val mPrefProvider: PreferenceProvider by instance()

    private lateinit var mActivityHomeBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        setContentView(mActivityHomeBinding.root)

        val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager, lifecycle)
        mActivityHomeBinding.viewPager.adapter = pagerAdapter
        //mActivityHomeBinding.viewPager.setPageTransformer(ZoomOutPageTransformer())
        mActivityHomeBinding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> mActivityHomeBinding.bottomNavigationView.menu.findItem(R.id.menu_single_crypto).isChecked =
                        true

                    1 -> mActivityHomeBinding.bottomNavigationView.menu.findItem(R.id.menu_crypto_market).isChecked =
                        true
                }
            }
        })

        mActivityHomeBinding.bottomNavigationView.setOnItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (mActivityHomeBinding.viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle
            // the Back button. This calls finish() on this activity and pops the back stack.
            AlertDialog.Builder(this).setTitle(getString(R.string.close_app))
                .setMessage(getString(R.string.are_you_sure_you_want_to_close))
                .setPositiveButton(R.string.ok) { _, _ -> finish() }
                .setNegativeButton(R.string.cancel, null).show()
        } else {
            // Otherwise, select the previous step.
            mActivityHomeBinding.viewPager.currentItem =
                mActivityHomeBinding.viewPager.currentItem - 1
        }
    }

    override fun onDestroy() {
        stopService(Intent(this@HomeActivity, SoundService::class.java))
        super.onDestroy()
    }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_single_crypto -> mActivityHomeBinding.viewPager.currentItem = 0
            R.id.menu_crypto_market -> mActivityHomeBinding.viewPager.currentItem = 1
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        setLanguageForApp(mPrefProvider.getLanguage()!!)
        if (mPrefProvider.getSoundEnabled()) {
            if (!isMyServiceRunning(SoundService::class.java)) startService(
                Intent(
                    this,
                    SoundService::class.java
                )
            )
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<SoundService>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun setLanguageForApp(selectedLanguageCode: String) {
        val locale = Locale(selectedLanguageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext!!.resources.updateConfiguration(config, baseContext!!.resources.displayMetrics)
    }
}