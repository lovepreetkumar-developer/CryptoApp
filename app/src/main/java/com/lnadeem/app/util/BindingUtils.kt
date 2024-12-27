package com.lnadeem.app.util

import android.annotation.SuppressLint
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.lnadeem.app.R
import java.text.SimpleDateFormat
import java.util.*

class BindingUtils {

    companion object {

        @BindingAdapter(value = ["loadWeatherIcon"])
        @JvmStatic
        fun loadWeatherIcon(imageView: AppCompatImageView, icon: String) {

            Glide.with(imageView.context)
                .load("https://openweathermap.org/img/wn/${icon}@2x.png")
                .disallowHardwareConfig().centerCrop().error(R.drawable.ic_cloud)
                .placeholder(R.drawable.ic_cloud).into(imageView)
        }

        @SuppressLint("SetTextI18n")
        @BindingAdapter(value = ["setWeatherDate"])
        @JvmStatic
        fun setWeatherDate(appCompatTextView: AppCompatTextView, timeStamp: Int) {
            try {
                val sdf = SimpleDateFormat("HH aa dd MMM", Locale.getDefault())
                val netDate = Date(timeStamp.toLong() * 1000)
                appCompatTextView.text = sdf.format(netDate)
            } catch (e: Exception) {
                appCompatTextView.text = "10 AM 08 MAY"
                e.toString()
            }
        }

        @BindingAdapter(value = ["set_selected_true"])
        @JvmStatic
        fun setSelectedTrue(textView: AppCompatTextView, string: String?) {
            textView.isSelected = true
        }

        @BindingAdapter(value = ["loadImage"])
        @JvmStatic
        fun loadImage(imageView: AppCompatImageView, imageUrl: String?) {
            if (!imageUrl.isNullOrBlank()) {
                Glide.with(imageView.context)
                    .load(imageUrl)
                    .disallowHardwareConfig().error(R.drawable.ic_app_logo)
                    .placeholder(R.drawable.ic_app_logo).into(imageView)
            }
        }
    }
}