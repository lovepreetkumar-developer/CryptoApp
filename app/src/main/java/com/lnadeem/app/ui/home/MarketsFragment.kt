package com.lnadeem.app.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.lnadeem.app.R
import com.lnadeem.app.base.BaseCallback
import com.lnadeem.app.base.BaseFragment
import com.lnadeem.app.data.preferences.PreferenceProvider
import com.lnadeem.app.databinding.FragmentMarketsBinding
import com.lnadeem.app.models.MarketDataModel
import com.lnadeem.app.models.MarketsResponse
import com.lnadeem.app.ui.fonty.Fonty
import com.lnadeem.app.util.MyApiException
import com.lnadeem.app.util.NoInternetException
import com.lnadeem.app.util.showMessageDialog
import com.lnadeem.app.util.valueIsNeitherNullNorEmpty
import com.lnadeem.app.view_models.HomeViewModel
import com.lnadeem.app.vm_factories.HomeViewModelFactory
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.collections.HashMap

class MarketsFragment : BaseFragment<FragmentMarketsBinding>(), KodeinAware {

    override val kodein by kodein()

    private val mFactory: HomeViewModelFactory by instance()
    private val mPrefProvider: PreferenceProvider by instance()

    private lateinit var mHomeViewModel: HomeViewModel

    private var mMarketsAdapter = GroupAdapter<GroupieViewHolder>()

    override fun getFragmentLayout(): Int = R.layout.fragment_markets

    override fun onFragmentCreateView(savedInstanceState: Bundle?) {
        super.onFragmentCreateView(savedInstanceState)
        initView()
    }

    private fun initView() {
        mHomeViewModel = ViewModelProvider(this, mFactory)[HomeViewModel::class.java]
        mBinding.rvMarkets.adapter = mMarketsAdapter

        mBinding.swipeRefreshLayout.isRefreshing = true
        getMarketsAPI()

        mBinding.swipeRefreshLayout.setOnRefreshListener {
            getMarketsAPI()
        }

        Fonty.setFonts(mBinding.swipeRefreshLayout)
    }

    private fun getMarketsAPI() {
        mMarketsAdapter.clear()
        lifecycleScope.launch {
            try {
                val queryMap = HashMap<String, String>()
                queryMap["order"] = "market_cap_desc"
                queryMap["vs_currency"] = mPrefProvider.getCurrency()!!
                val response = mHomeViewModel.getMarkets(queryMap)
                setData(response)
                mBinding.swipeRefreshLayout.isRefreshing = false
            } catch (ex: MyApiException) {
                mBinding.swipeRefreshLayout.isRefreshing = false
                ex.printStackTrace()
                if (valueIsNeitherNullNorEmpty(ex.message)) requireContext().showMessageDialog(ex.message.toString())
            } catch (ex: NoInternetException) {
                mBinding.swipeRefreshLayout.isRefreshing = false
                ex.printStackTrace()
            } catch (ex: Exception) {
                mBinding.swipeRefreshLayout.isRefreshing = false
                ex.printStackTrace()
            }
        }
    }

    private fun setData(response: MarketsResponse) {
        mMarketsAdapter.addAll(response.toMarketItems())
    }

    private fun List<MarketDataModel>.toMarketItems(): List<BindableItemMarket> {
        return this.map {
            BindableItemMarket(it, mPrefProvider)
        }
    }

    override fun onResume() {
        super.onResume()
        setLanguageForApp(mPrefProvider.getLanguage()!!)
        if (mPrefProvider.getCurrentThemeColor() != 0)
            mBinding.swipeRefreshLayout.setBackgroundColor(mPrefProvider.getCurrentThemeColor())
    }

    private fun setLanguageForApp(selectedLanguageCode: String) {
        val locale = Locale(selectedLanguageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }
}