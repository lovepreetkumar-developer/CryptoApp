package com.lnadeem.app.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import com.lnadeem.app.R
import com.lnadeem.app.util.showToast
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

abstract class BaseFragment<V : ViewDataBinding> : Fragment(), KodeinAware {

    protected lateinit var mBinding: V
    protected lateinit var baseContext: Context


    override val kodein by kodein()

    //protected val mPrefProvider: PreferenceProvider by instance()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.baseContext = context
    }

    protected open fun setBaseCallback(baseCallback: BaseCallback?) {
        mBinding.setVariable(BR.callback, baseCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        mBinding = DataBindingUtil.inflate(
            inflater, getFragmentLayout(), container, false
        )

        onFragmentCreateView(savedInstanceState)

        return mBinding.root
    }

    protected fun showToast(message: String) {
        baseContext.showToast(message)
    }

    @CallSuper
    protected open fun onFragmentCreateView(savedInstanceState: Bundle?) {
    }

    protected abstract fun getFragmentLayout(): Int

    protected open fun goBack() {
        requireActivity().onBackPressed()
        requireActivity().overridePendingTransition(
            R.anim.activity_back_in, R.anim.activity_back_out
        )
    }

    protected open fun goNextAnimation() {
        requireActivity().overridePendingTransition(
            R.anim.activity_in, R.anim.activity_out
        )
    }

    protected open fun goBackAnimation() {
        requireActivity().overridePendingTransition(
            R.anim.activity_back_in, R.anim.activity_back_out
        )
    }
}