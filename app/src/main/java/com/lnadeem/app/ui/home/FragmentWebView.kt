package com.lnadeem.app.ui.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.DialogFragment
import com.lnadeem.app.R
import com.lnadeem.app.base.BaseCallback
import com.lnadeem.app.data.preferences.PreferenceProvider
import com.lnadeem.app.databinding.FragmentWebViewBinding
import com.lnadeem.app.ui.fonty.Fonty
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.*

class FragmentWebView : DialogFragment(), KodeinAware {

    override val kodein by kodein()

    private val mPrefProvider: PreferenceProvider by instance()

    private lateinit var mBinding: FragmentWebViewBinding

    override fun onStart() {
        super.onStart()
        dialog?.let { dialogInstance ->
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialogInstance.window?.setLayout(width, height)
            dialogInstance.window?.setWindowAnimations(R.style.WebViewTheme)
            dialogInstance.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        mBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_web_view, container, false
        )
        mBinding.setVariable(BR.callback, baseCallback)

        return mBinding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.webView.loadUrl("https://www.coingecko.com/en/api")
        mBinding.webView.isFocusable = true
        mBinding.webView.isFocusableInTouchMode = true
        mBinding.webView.settings.javaScriptEnabled = true
        mBinding.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        mBinding.webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        mBinding.webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        mBinding.webView.settings.domStorageEnabled = true
        mBinding.webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        mBinding.webView.settings.databaseEnabled = true
        mBinding.webView.settings.setSupportMultipleWindows(false)
        mBinding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Timber.d("onPageFinished")
            }
        }
        Fonty.setFonts(mBinding.llParent)
    }

    private val baseCallback = object : BaseCallback {
        override fun onClick(view: View) {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        setLanguageForApp(mPrefProvider.getLanguage()!!)
    }

    private fun setLanguageForApp(selectedLanguageCode: String) {
        val locale = Locale(selectedLanguageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context?.let {
            it.resources?.updateConfiguration(config, it.resources.displayMetrics)
        }
    }
}