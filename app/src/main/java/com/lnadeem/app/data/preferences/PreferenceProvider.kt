package com.lnadeem.app.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class PreferenceProvider(context: Context) {

    private val appContext = context.applicationContext
    private val mSharedPreference: SharedPreferences =
        context.applicationContext.getSharedPreferences("WeatherApp", Context.MODE_PRIVATE)

    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun clear(): Boolean {
        return preferences.edit().clear().commit()
    }

    fun setSoundEnabled(value: Boolean) {
        return preferences.edit().putBoolean(
            "SOUND_ENABLED", value
        ).apply()
    }

    fun getSoundEnabled(): Boolean {
        return preferences.getBoolean("SOUND_ENABLED", false)
    }

    fun setLanguage(language: String?): Boolean {
        return preferences.edit().putString(
            "LANGUAGE", language
        ).commit()
    }

    fun getLanguage(): String? {
        return preferences.getString("LANGUAGE", "en")
    }

    fun setCurrency(currency: String?): Boolean {
        return preferences.edit().putString(
            "CURRENCY", currency
        ).commit()
    }

    fun getCurrency(): String? {
        return preferences.getString("CURRENCY", "USD")
    }

    fun setCurrentThemeColor(themeColor: Int) {
        preferences.edit().putInt(
            "THEME_COLOR", themeColor
        ).apply()
    }

    fun getCurrentThemeColor(): Int {
        return preferences.getInt("THEME_COLOR", 0)
    }

    fun setFontName(language: String?): Boolean {
        return preferences.edit().putString(
            "FONT_NAME", language
        ).commit()
    }

    fun getFontName(): String? {
        return preferences.getString("FONT_NAME", "Inter")
    }

    fun setCelsius(value: Boolean) {
        return preferences.edit().putBoolean(
            "CELSIUS", value
        ).apply()
    }

    fun getCelsius(): Boolean {
        return preferences.getBoolean("CELSIUS", true)
    }
}