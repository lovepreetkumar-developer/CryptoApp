package com.lnadeem.app.data.respositories

import android.content.Context
import com.lnadeem.app.data.network.Apis
import com.lnadeem.app.data.network.SafeApiRequest
import com.lnadeem.app.models.MarketsResponse

class CommonRepository(
    context: Context,
    private val api: Apis
) : SafeApiRequest(context) {

    suspend fun getMarkets(
        queryMap: HashMap<String, String>
    ): MarketsResponse {
        return apiRequest { api.getMarkets(queryMap) }
    }

}