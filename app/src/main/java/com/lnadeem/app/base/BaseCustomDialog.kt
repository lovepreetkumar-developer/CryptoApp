package com.lnadeem.app.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lnadeem.app.R
import androidx.databinding.library.baseAdapters.BR
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class BaseCustomDialog<V : ViewDataBinding>(context: Context) :
    Dialog(context, R.style.CustomDialog), KodeinAware {

    override val kodein by kodein()

    private var binding: V? = null

    @LayoutRes
    private var layoutId = 0
    private var listener: DialogListener? = null

    constructor(
        context: Context,
        @LayoutRes layoutId: Int,
        listener: DialogListener
    ) : this(context) {
        this.layoutId = layoutId
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setContentView(binding!!.root)
    }

    fun getBinding(): V {
        init()
        return binding!!
    }

    private fun init() {
        if (binding == null) {
            binding =
                DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, null, false)
            if (listener != null) binding!!.setVariable(BR.callback, listener)
        }
    }

    interface DialogListener {
        fun onViewClick(view: View?)
    }
}