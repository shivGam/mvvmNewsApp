package com.androiddevs.mvvmnewsapp.api

import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.util.Contants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getLiveFeed(
        @Query("country")
        countryCode: String = "in",
        @Query("page")
        pageNumber:Int = 1,
        @Query("apikey")
        apikey:String=API_KEY
    ):Response<NewsResponse>

    @GET("v2/everything")
    suspend fun getSearchNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber:Int = 1,
        @Query("apikey")
        apikey:String=API_KEY
    ):Response<NewsResponse>

}