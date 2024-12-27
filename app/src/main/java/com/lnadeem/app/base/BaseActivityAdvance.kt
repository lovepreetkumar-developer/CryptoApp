package com.lnadeem.app.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.library.baseAdapters.BR
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lnadeem.app.R
import org.kodein.di.KodeinAware

abstract class BaseActivityAdvance<V : ViewDataBinding> : AppCompatActivity(), KodeinAware {

    protected lateinit var binding: V
    protected var context: Context? = null
    // private val mPrefProvider: PreferenceProvider by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        binding = DataBindingUtil.setContentView(this, getContentView())
        this.context = this
        onViewReady(savedInstanceState, intent)

        try {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goBackAnimation()
    }

    protected open fun setBaseCallback(baseCallback: BaseCallback?) {
        binding.setVariable(BR.callback, baseCallback)
    }

    @CallSuper
    open fun onViewReady(savedInstanceState: Bundle?, intent: Intent?) {
    }

    abstract fun getContentView(): Int

    protected open fun goBack() {
        super.onBackPressed()
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out)
    }

    protected open fun finishActivityWithBackAnim() {
        finish()
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out)
    }

    protected open fun goNextAnimation() {
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out)
    }

    protected open fun goNextSwipeAnimation() {
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out)
    }

    protected open fun goBackAnimation() {
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out)
    }
}