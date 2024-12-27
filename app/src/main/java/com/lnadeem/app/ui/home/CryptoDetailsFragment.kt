package com.lnadeem.app.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.lnadeem.app.R
import com.lnadeem.app.base.BaseCallback
import com.lnadeem.app.base.BaseFragment
import com.lnadeem.app.data.preferences.PreferenceProvider
import com.lnadeem.app.databinding.FragmentCryptoDetailsBinding
import com.lnadeem.app.models.MarketDataModel
import com.lnadeem.app.models.MarketsResponse
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
import timber.log.Timber
import java.util.*

class CryptoDetailsFragment : BaseFragment<FragmentCryptoDetailsBinding>(), KodeinAware {

    override val kodein by kodein()

    private val mFactory: HomeViewModelFactory by instance()
    private val mPrefProvider: PreferenceProvider by instance()

    private lateinit var mHomeViewModel: HomeViewModel

    override fun getFragmentLayout(): Int = R.layout.fragment_crypto_details

    override fun onFragmentCreateView(savedInstanceState: Bundle?) {
        super.onFragmentCreateView(savedInstanceState)
        initView()
    }

    private fun initView() {
        setBaseCallback(baseCallback)
        mHomeViewModel = ViewModelProvider(this, mFactory)[HomeViewModel::class.java]

        getMarketsAPI()

    }

    private fun getMarketsAPI() {
        lifecycleScope.launch {
            try {
                val queryMap = HashMap<String, String>()
                queryMap["order"] = "market_cap_desc"
                queryMap["vs_currency"] = mPrefProvider.getCurrency()!!
                val response = mHomeViewModel.getMarkets(queryMap)
                setMarketsOnSpinner(response)
            } catch (ex: MyApiException) {
                ex.printStackTrace()
                if (valueIsNeitherNullNorEmpty(ex.message)) requireContext().showMessageDialog(ex.message.toString())
            } catch (ex: NoInternetException) {
                ex.printStackTrace()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun setMarketsOnSpinner(response: MarketsResponse) {
        mHomeViewModel.mAllMarkets.clear()
        mHomeViewModel.mAllMarkets.addAll(response)
        val adapter = MarketsSpinnerDropDownAdapter(
            requireContext(), R.layout.spinner_view_text, response
        )
        mBinding.spinnerMarkets.adapter = adapter

        mBinding.spinnerMarkets.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    setCryptoData(mHomeViewModel.mAllMarkets[position])
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    Timber.d("onNothingSelected")
                }
            }
    }

    private val baseCallback = object : BaseCallback {
        override fun onClick(view: View) {
            when (view.id) {
                R.id.iv_menu -> showOpenMenu()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCryptoData(marketDataModel: MarketDataModel) {
        Glide.with(this).load(marketDataModel.image).disallowHardwareConfig().centerCrop()
            .error(R.drawable.ic_app_logo).placeholder(R.drawable.ic_app_logo).into(mBinding.ivIcon)
        mBinding.tvCurrentPrice.text =
            mPrefProvider.getCurrency() + " " + marketDataModel.current_price
        mBinding.tvSymbol.text = marketDataModel.symbol
        mBinding.tvMarketCap.text = marketDataModel.market_cap.toString()
        mBinding.tvTotalVolume.text = marketDataModel.total_volume.toString()
        mBinding.tvHigh.text = marketDataModel.high_24h.toString()
        mBinding.tvLow.text = marketDataModel.low_24h.toString()
    }

    private fun showOpenMenu() {
        val popupMenu = PopupMenu(requireContext(), mBinding.ivMenu)
        popupMenu.menuInflater.inflate(R.menu.option_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_usd -> {
                    mPrefProvider.setCurrency("USD")
                    getMarketsAPI()
                }
                R.id.action_euro -> {
                    mPrefProvider.setCurrency("EUR")
                    getMarketsAPI()
                }
                R.id.action_language_settings -> {
                    startActivity(Intent(requireContext(), LanguageSettingsActivity::class.java))
                }
                R.id.action_sound_settings -> {
                    startActivity(Intent(requireContext(), SoundSettingsActivity::class.java))
                }
                R.id.action_web_view -> {
                    FragmentWebView().show(childFragmentManager, "FragmentWebView")
                }
            }
            goNextAnimation()
            true
        }
        popupMenu.show()
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
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }
}