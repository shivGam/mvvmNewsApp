package com.androiddevs.mvvmnewsapp.userinterface.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.userinterface.NewsActivity
import com.androiddevs.mvvmnewsapp.userinterface.NewsViewModel

class HomeNewsFragment: Fragment(R.layout.fragment_home_news) {
    lateinit var viewModel: NewsViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as NewsActivity).viewModel
    }
}