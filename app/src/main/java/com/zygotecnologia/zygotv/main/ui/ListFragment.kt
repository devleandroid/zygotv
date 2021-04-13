package com.zygotecnologia.zygotv.main.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zygotecnologia.zygotv.R
import com.zygotecnologia.zygotv.databinding.FragmentListBinding
import com.zygotecnologia.zygotv.main.MainAdapter
import com.zygotecnologia.zygotv.model.Show
import com.zygotecnologia.zygotv.network.TmdbApi
import com.zygotecnologia.zygotv.network.TmdbClient
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class ListFragment : Fragment(), CoroutineScope, MainAdapter.OnClickDetailsInterface {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    private val tmdbApi = TmdbClient.getInstance()
    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: ViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        launch(Dispatchers.IO) { loadShows() }
        setupViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setupDataBinding(inflater, container)
        setupRecyclerViews()
        return getRoot()
    }

    private fun getViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
    }

    private fun setupViewModel() {
        getViewModel()
    }

    private fun setMoviesList(elementList: List<Show>) {
        (binding.rvShowList.adapter as MainAdapter).setElementList(elementList)
    }

    private fun setupDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
    }

    private fun setupRecyclerViews() {
        binding.rvShowList.layoutManager = createGridLayoutManager()
    }

    private fun getRoot(): View? {
        return binding.root
    }

    private fun createGridLayoutManager(): GridLayoutManager? {
        val manager = GridLayoutManager(context, getSpanCount())
        manager.orientation = RecyclerView.VERTICAL
        return manager
    }

    private fun getSpanCount(): Int {
        val orientation = resources.configuration.orientation
        return if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            1
        } else {
            2
        }
    }

    private suspend fun loadShows() {
        val genres =
            tmdbApi
                .fetchGenresAsync(TmdbApi.TMDB_API_KEY, "BR")
                ?.genres
                ?: emptyList()
        val shows =
            tmdbApi
                .fetchPopularShowsAsync(TmdbApi.TMDB_API_KEY, "BR")
                ?.results
                ?.map { show ->
                    show.copy(genres = genres.filter { show.genreIds?.contains(it.id) == true })
                }
                ?: emptyList()

        withContext(Dispatchers.Main) {
            binding.rvShowList.adapter = MainAdapter(this@ListFragment, shows)
        }
    }

    private fun setDetailElement(clickedItem: Int, show: List<Show>) {
        viewModel.setDetailMovie(clickedItem, show)
    }

    private fun navigateToDrinkListFragment() {
        val action = ListFragmentDirections.actionListFragmentToDetailFragment()

        NavHostFragment.findNavController(this).navigate(action)
    }

    override fun onItemClick(clickedItem: Int, shows: List<Show>) {
        setDetailElement(clickedItem, shows)
        navigateToDrinkListFragment()
    }
}