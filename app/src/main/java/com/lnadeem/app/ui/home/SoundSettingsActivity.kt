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
import com.lnadeem.app.databinding.ActivitySoundSettingsBinding
import com.lnadeem.app.ui.fonty.Fonty
import com.lnadeem.app.util.SoundService
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*

open class SoundSettingsActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    private val mPrefProvider: PreferenceProvider by instance()

    private lateinit var mActivitySoundSettingsBinding: ActivitySoundSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivitySoundSettingsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_sound_settings)
        mActivitySoundSettingsBinding.callback = baseCallback

        setContentView(mActivitySoundSettingsBinding.root)

        if (mPrefProvider.getSoundEnabled()) {
            mActivitySoundSettingsBinding.rbOn.isChecked = true
        } else {
            mActivitySoundSettingsBinding.rbOff.isChecked = true
        }

        mActivitySoundSettingsBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbOn) {
                mPrefProvider.setSoundEnabled(true)
                soundWork()
            } else {
                mPrefProvider.setSoundEnabled(false)
                soundWork()
            }
        }

        Fonty.setFonts(this)
    }

    private fun soundWork() {
        if (mPrefProvider.getSoundEnabled()) {
            startService(Intent(this, SoundService::class.java))
        } else {
            stopService(Intent(this, SoundService::class.java))
        }
    }

    private val baseCallback = object : BaseCallback {
        override fun onClick(view: View) {
            super.onClick(view)
            when (view.id) {
                R.id.imgBack -> onBackPressed()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setLanguageForApp(mPrefProvider.getLanguage()!!)
        if (mPrefProvider.getCurrentThemeColor() != 0)
            mActivitySoundSettingsBinding.llBackground.setBackgroundColor(mPrefProvider.getCurrentThemeColor())
    }

    private fun setLanguageForApp(selectedLanguageCode: String) {
        val locale = Locale(selectedLanguageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext!!.resources.updateConfiguration(config, baseContext!!.resources.displayMetrics)
    }
}