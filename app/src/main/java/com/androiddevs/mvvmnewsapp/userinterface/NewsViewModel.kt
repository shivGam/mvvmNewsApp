package com.androiddevs.mvvmnewsapp.userinterface

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.Repository.NewsRepository
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository : NewsRepository
): ViewModel(){

    val liveFeed : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var liveFeedPage = 1

    init{
        getLiveFeed("in")
    }

    fun getLiveFeed(countryCode: String)= viewModelScope.launch{

        liveFeed.postValue(Resource.Loading())
        val response = newsRepository.getLiveFeed(countryCode,liveFeedPage)
        liveFeed.postValue(handleLiveFeedResponse(response))

    }

    private fun handleLiveFeedResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful){
            response.body()?.let{ resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}