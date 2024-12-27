package com.lnadeem.app.ui.home

import android.view.View
import androidx.databinding.library.baseAdapters.BR
import com.xwray.groupie.viewbinding.BindableItem
import com.lnadeem.app.R
import com.lnadeem.app.data.preferences.PreferenceProvider
import com.lnadeem.app.databinding.ItemMarketBinding
import com.lnadeem.app.models.MarketDataModel
import com.lnadeem.app.ui.fonty.Fonty

class BindableItemMarket(
    private val model: MarketDataModel,
    private val preferenceProvider: PreferenceProvider
) : BindableItem<ItemMarketBinding>() {

    override fun getLayout(): Int = R.layout.item_market

    override fun bind(viewBinding: ItemMarketBinding, position: Int) {
        viewBinding.setVariable(BR.model, model)
        viewBinding.setVariable(BR.preferenceProvider, preferenceProvider)
        viewBinding.setVariable(BR.pos, position)
        Fonty.setFonts(viewBinding.llParent)
    }

    override fun initializeViewBinding(view: View): ItemMarketBinding {
        return ItemMarketBinding.bind(view)
    }
}