package com.zygotecnologia.zygotv.main.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zygotecnologia.zygotv.model.Show

class ViewModel(application: Application) : AndroidViewModel(application) {

    private var detailElement = MutableLiveData<Show>()
    private lateinit var elementList: LiveData<List<Show>>
    private var watchList: LiveData<List<Show>> = MutableLiveData<List<Show>>()
    private var movieSearchList = MutableLiveData<List<Show>>()
    private var tvShowSearchList = MutableLiveData<List<Show>>()

    private var query: String = ""

    fun setDetailMovie(position: Int, show: List<Show>) {
        movieSearchList.value = show
        when (show) {

            show -> setDetailMovie(position)
            else -> setDetailMovieAndTvShow(position)
        }

        checkDetailMovieInWatchList()
    }

    private fun setDetailMovie(position: Int) {
        detailElement.value = movieSearchList.value?.get(position)
    }


    fun getDetailMovie(): MutableLiveData<Show> {
        return detailElement
    }

    private fun setDetailMovieAndTvShow(position: Int) {
        detailElement.value = if (query.isEmpty()) {
            elementList.value?.get(position)
        } else {
            tvShowSearchList.value?.get(position)
        }
    }

    private fun checkDetailMovieInWatchList() {
        watchList.value?.forEach { movie -> if (detailElement.value?.id == movie.id) true }
    }
}