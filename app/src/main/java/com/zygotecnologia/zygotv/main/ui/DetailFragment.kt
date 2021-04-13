package com.zygotecnologia.zygotv.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.zygotecnologia.zygotv.R
import com.zygotecnologia.zygotv.databinding.FragmentDetailBinding
import com.zygotecnologia.zygotv.model.Show
import com.zygotecnologia.zygotv.utils.ImageUrlBuilder


class DetailFragment : Fragment() {

    private lateinit var viewModel: ViewModel
    private lateinit var binding: FragmentDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setupDataBinding(inflater, container)
        return getRoot()
    }

    private fun setupDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
    }

    private fun getRoot(): View {
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
    }

    private fun updateUI(show: Show?) {
        setName(show?.name)
        setOverview(show?.overview)
        setPoster(show?.posterPath)
        setVoteAverage(show?.voteCount.toString())

    }

    private fun setupViewModel() {
        getViewModel()
        observeDetailElement()
    }

    private fun observeDetailElement() {
        viewModel.getDetailMovie().observe(
            viewLifecycleOwner,
            Observer {
                updateUI(it)
            }
        )
    }

    private fun getViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
    }

    private fun setName(name: String?) {
        binding.name.text = name
    }

    private fun setOverview(overview: String?) {
        binding.overview.text = overview
    }

    private fun setPoster(posterPath: String?) {
        Picasso.get().load(posterPath?.let { ImageUrlBuilder.buildBackdropUrl(it) })
            .into(binding.posterDetails.poster)
    }

    private fun setVoteAverage(voteAverage: String?) {
        val voteAverageText = "Vote average: $voteAverage"
        binding.voteAverage.text = voteAverageText
    }


}