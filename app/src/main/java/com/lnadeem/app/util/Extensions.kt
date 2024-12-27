package com.lnadeem.app.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.lnadeem.app.R
import com.lnadeem.app.base.BaseCustomDialog
import com.lnadeem.app.databinding.DialogMessageBinding


@Suppress("DEPRECATION")
fun Activity.hideStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window?.insetsController?.hide(WindowInsets.Type.statusBars())
    } else {
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun valueIsNeitherNullNorEmpty(value: String?): Boolean {
    return !value.isNullOrEmpty()
}

var messageDialog: BaseCustomDialog<DialogMessageBinding>? = null

fun Context.showMessageDialog(message: String) {
    messageDialog?.dismiss()
    messageDialog = BaseCustomDialog(
        this,
        R.layout.dialog_message,
        object : BaseCustomDialog.DialogListener {
            override fun onViewClick(view: View?) {
                messageDialog?.dismiss()
            }
        })
    messageDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    messageDialog?.getBinding()?.tvMessage?.text = message
    messageDialog?.setCancelable(false)
    messageDialog?.show()
}

fun AppCompatAutoCompleteTextView.getViewText(): String {
    return text.toString()
}

fun AppCompatEditText.getViewText(): String {
    return text.toString()
}

fun AppCompatTextView.getViewText(): String {
    return text.toString()
}

fun AppCompatTextView.setCustomTextColor(color: Int) {
    setTextColor(
        ContextCompat.getColor(
            this.context,
            color
        )
    )
}
