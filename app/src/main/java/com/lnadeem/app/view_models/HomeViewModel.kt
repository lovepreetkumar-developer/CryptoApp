package com.lnadeem.app.view_models

import androidx.lifecycle.ViewModel
import com.lnadeem.app.data.respositories.CommonRepository
import com.lnadeem.app.models.MarketDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val mainRepository: CommonRepository
) : ViewModel() {

    internal var mAllMarkets = mutableListOf<MarketDataModel>()

    suspend fun getMarkets(
        queryMap: HashMap<String, String>
    ) = withContext(Dispatchers.IO) {
        mainRepository.getMarkets(queryMap)
    }

}