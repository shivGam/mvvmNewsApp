package com.androiddevs.mvvmnewsapp.userinterface

import androidx.lifecycle.ViewModel
import com.androiddevs.mvvmnewsapp.Repository.NewsRepository

class NewsViewModel(
    val newsRepository : NewsRepository
): ViewModel(){
}