package com.lnadeem.app.ui.home

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.lnadeem.app.R
import com.lnadeem.app.base.BaseCallback
import com.lnadeem.app.data.preferences.PreferenceProvider
import com.lnadeem.app.databinding.ActivityTemperatureSettingsBinding
import com.lnadeem.app.ui.fonty.Fonty
import com.lnadeem.app.ui.welcome.SplashActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*


class TemperatureSettingsActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    private val mPrefProvider: PreferenceProvider by instance()

    private lateinit var mActivityTemperatureSettingsBinding: ActivityTemperatureSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivityTemperatureSettingsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_temperature_settings)
        mActivityTemperatureSettingsBinding.callback = baseCallback

        setContentView(mActivityTemperatureSettingsBinding.root)

        if (mPrefProvider.getCelsius()) {
            mActivityTemperatureSettingsBinding.rbCelsius.isChecked = true
        } else {
            mActivityTemperatureSettingsBinding.rbFahrenheit.isChecked = true
        }

        mActivityTemperatureSettingsBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbCelsius) {
                mPrefProvider.setCelsius(true)
                startSplash()
            } else {
                mPrefProvider.setCelsius(false)
                startSplash()
            }
        }

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

    private fun startSplash() {
        startActivity(Intent(
            this, SplashActivity::class.java
        ).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    override fun onResume() {
        super.onResume()
        setLanguageForApp(mPrefProvider.getLanguage()!!)
        if (mPrefProvider.getCurrentThemeColor() != 0) mActivityTemperatureSettingsBinding.llBackground.setBackgroundColor(
            mPrefProvider.getCurrentThemeColor()
        )
    }

    private fun setLanguageForApp(selectedLanguageCode: String) {
        val locale = Locale(selectedLanguageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext!!.resources.updateConfiguration(config, baseContext!!.resources.displayMetrics)
    }
}