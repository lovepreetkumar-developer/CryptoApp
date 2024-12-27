package com.lnadeem.app

import android.app.Application
import android.content.Intent
import com.lnadeem.app.data.network.Apis
import com.lnadeem.app.data.network.NetworkConnectionInterceptor
import com.lnadeem.app.data.preferences.PreferenceProvider
import com.lnadeem.app.data.respositories.CommonRepository
import com.lnadeem.app.ui.fonty.Fonty.Companion.context
import com.lnadeem.app.util.SoundService
import com.lnadeem.app.vm_factories.HomeViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


class MainApplication : Application(), KodeinAware {

    private val mPrefProvider: PreferenceProvider by instance()

    companion object {
        private var instance: MainApplication? = null
        fun getInstance(): MainApplication? {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (mPrefProvider.getFontName() == "Inter") {
            context(this).normalTypeface("inter_regular.ttf").boldTypeface("inter_bold.ttf").build()
        } else {
            context(this).normalTypeface("great_vibes.otf").boldTypeface("great_vibes.otf").build()
        }
        if (mPrefProvider.getSoundEnabled()) {
            startService(Intent(this, SoundService::class.java))
        }
    }

    override val kodein = Kodein.lazy {

        import(androidXModule(this@MainApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { Apis(instance()) }
        bind() from singleton { CommonRepository(instance(), instance()) }
        bind() from singleton { HomeViewModelFactory(instance()) }
    }
}