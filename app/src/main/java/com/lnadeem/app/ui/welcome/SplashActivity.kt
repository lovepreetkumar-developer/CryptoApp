package com.lnadeem.app.ui.welcome

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.lnadeem.app.R
import com.lnadeem.app.data.preferences.PreferenceProvider
import com.lnadeem.app.databinding.ActivitySplashBinding
import com.lnadeem.app.ui.home.HomeActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    private val mPrefProvider: PreferenceProvider by instance()

    private var mHandler: Handler? = null

    private lateinit var mActivitySplashBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        setContentView(mActivitySplashBinding.root)
        mHandler = Handler(Looper.getMainLooper())
        mHandler?.postDelayed(mRunnable, 2000)
    }

    override fun onResume() {
        super.onResume()
        if (mPrefProvider.getCurrentThemeColor() != 0)
            mActivitySplashBinding.rlBackground.setBackgroundColor(mPrefProvider.getCurrentThemeColor())
    }

    override fun onDestroy() {
        mHandler?.removeCallbacks(mRunnable)
        super.onDestroy()
    }

    private val mRunnable = Runnable {
        startActivity(Intent(
            this, HomeActivity::class.java
        ).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out)
        })
    }
}