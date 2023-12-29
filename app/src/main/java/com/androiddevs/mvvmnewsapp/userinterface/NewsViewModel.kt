package com.androiddevs.mvvmnewsapp.userinterface

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.NewsApplication
import com.androiddevs.mvvmnewsapp.Repository.NewsRepository
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    val app : Application,
    val newsRepository : NewsRepository
): AndroidViewModel(app){

    val liveFeed : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var liveFeedPage = 1
    var liveFeedResponse:NewsResponse ?=null

    val searchNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse:NewsResponse ?=null

    init{
        getLiveFeed("in")
    }

    fun getSearchNews(searchQuery: String)= viewModelScope.launch{

        safeGetSearchNews(searchQuery)

    }

    fun getLiveFeed(countryCode: String)= viewModelScope.launch{

        safeGetLiveFeed(countryCode)

    }


    private fun handleLiveFeedResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful){
            response.body()?.let{ resultResponse ->
                liveFeedPage++
                if(liveFeedResponse == null){
                    liveFeedResponse= resultResponse
                }
                else{
                    val oldArticles = liveFeedResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(liveFeedResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful){
            response.body()?.let{ resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private suspend fun safeGetSearchNews(searchQuery: String) {
        searchNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = newsRepository.getSearchNews(searchQuery,searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            }
            else{
                searchNews.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t : Throwable){
            when(t){
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }

    }

    private suspend fun safeGetLiveFeed(countryCode: String) {
        liveFeed.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getLiveFeed(countryCode, liveFeedPage)
                liveFeed.postValue(handleLiveFeedResponse(response))
            } else {
                liveFeed.postValue(Resource.Error("No Internet Connection"))
            }
        }catch(t:Throwable){
            when(t){
                is IOException -> liveFeed.postValue(Resource.Error("Network Failure"))
                else ->liveFeed.postValue(Resource.Error("Conversion Error"))
            }
        }

    }

    private fun hasInternetConnection() : Boolean{
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)-> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)-> true
                else -> false
            }
        }
        return false
    }

}