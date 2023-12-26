package com.androiddevs.mvvmnewsapp.userinterface.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapters.NewsAdapter
import com.androiddevs.mvvmnewsapp.userinterface.NewsActivity
import com.androiddevs.mvvmnewsapp.userinterface.NewsViewModel
import com.androiddevs.mvvmnewsapp.util.Contants.Companion.TIME_DELAY
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.android.synthetic.main.fragment_home_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeNewsFragment: Fragment(R.layout.fragment_home_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as NewsActivity).viewModel
        setupRV()

        var job:Job?=null

        etSearch.addTextChangedListener{ editable->
            job?.cancel()
            job = MainScope().launch {
                delay(TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.getSearchNews(editable.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success ->{
                    hideProgressBar()
                    response.data?.let{
                        newsAdapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Error ->{
                    hideProgressBar()
                    response.message?.let{
                        Log.e("Home Page","Error: $it ")
                    }
                }
                is Resource.Loading ->{
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar(){
        paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun setupRV(){
        newsAdapter= NewsAdapter()
        rvSearchNews.apply {
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)
        }
    }
}