package com.androiddevs.mvvmnewsapp.Repository

import com.androiddevs.mvvmnewsapp.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.database.ArticleDatabase
import java.util.Locale.IsoCountryCode

class NewsRepository (
    val db: ArticleDatabase
    ){

    suspend fun getLiveFeed(countryCode:String,pageNumber:Int)=
        RetrofitInstance.api.getLiveFeed(countryCode,pageNumber)

}