package com.dicoding.dicodingevent.ui.finished

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
//import androidx.appcompat.widget.SearchView
//import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.dicodingevent.ListEventAdapter
import com.dicoding.dicodingevent.databinding.FragmentFinishedBinding
import com.dicoding.dicodingevent.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: FinishedViewModel
    private val listEventAdapter = ListEventAdapter { eventId ->
        DetailActivity.start(requireContext() , eventId)
    }

    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater , container , false)
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
        }
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        setRView()
        setViewModel()
        setSearchView()
    }

    private fun setSearchView() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _ , _ , _ ->
                val query = searchView.text.toString()
                searchQuery(query)
                true
            }
        }
    }

    private fun searchQuery(query: String) {
        with(binding) {
            if (query.isNotEmpty()) {
                viewModel.searchEvents(query)
                searchBar.setText(query)
            } else {
                viewModel.getEvent()
                searchBar.setText("")
            }
            searchView.hide()
            keyboardHide()
        }
    }

    private fun keyboardHide() {
        val hide =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        hide.hideSoftInputFromWindow(requireView().windowToken , 0)
    }

    private fun setRView() {
        binding.rvListFin.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = listEventAdapter
        }
    }
    private fun setViewModel() {
        viewModel = ViewModelProvider(this)[FinishedViewModel::class.java]

        viewModel.listEvents.observe(viewLifecycleOwner) { event ->
            listEventAdapter.submitList(event)

        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext() , errorMessage , Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}
