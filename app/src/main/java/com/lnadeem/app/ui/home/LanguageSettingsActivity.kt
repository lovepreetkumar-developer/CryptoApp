package com.lnadeem.app.ui.home

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.lnadeem.app.R
import com.lnadeem.app.base.BaseCallback
import com.lnadeem.app.data.preferences.PreferenceProvider
import com.lnadeem.app.databinding.ActivityLanguageSettingsBinding
import com.lnadeem.app.ui.fonty.Fonty
import com.lnadeem.app.ui.welcome.SplashActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.*
import kotlin.system.exitProcess

class LanguageSettingsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    KodeinAware {

    override val kodein by kodein()

    private val mPrefProvider: PreferenceProvider by instance()

    private lateinit var mActivityLanguageSettingsBinding: ActivityLanguageSettingsBinding

    private var mLanguages = arrayOf("Select Language", "English", "Russian")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivityLanguageSettingsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_language_settings)
        mActivityLanguageSettingsBinding.callback = baseCallback

        setContentView(mActivityLanguageSettingsBinding.root)

        mActivityLanguageSettingsBinding.spinnerLanguage.onItemSelectedListener = this

        val aa: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, mLanguages)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mActivityLanguageSettingsBinding.spinnerLanguage.adapter = aa

        Fonty.setFonts(this)
    }

    private val baseCallback = object : BaseCallback {
        override fun onClick(view: View) {
            super.onClick(view)
            when (view.id) {
                R.id.imgBack -> onBackPressed()
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (mActivityLanguageSettingsBinding.spinnerLanguage.selectedItemPosition) {
            1 -> {
                mPrefProvider.setLanguage("en")
                restartApp()
            }
            2 -> {
                mPrefProvider.setLanguage("ru")
                restartApp()
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Timber.d("test")
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun restartApp() {
        val mStartActivity = Intent(this@LanguageSettingsActivity, SplashActivity::class.java)
        val mPendingIntentId = 123456
        val mPendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                this,
                mPendingIntentId,
                mStartActivity,
                PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this,
                mPendingIntentId,
                mStartActivity,
                PendingIntent.FLAG_ONE_SHOT
            )
        }
        val mgr = getSystemService(ALARM_SERVICE) as AlarmManager
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent)
        exitProcess(0)
    }

    override fun onResume() {
        super.onResume()
        setLanguageForApp(mPrefProvider.getLanguage()!!)
        if (mPrefProvider.getCurrentThemeColor() != 0)
            mActivityLanguageSettingsBinding.llBackground.setBackgroundColor(mPrefProvider.getCurrentThemeColor())
    }

    private fun setLanguageForApp(selectedLanguageCode: String) {
        val locale = Locale(selectedLanguageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext!!.resources.updateConfiguration(config, baseContext!!.resources.displayMetrics)
    }
}