package com.dicoding.dicodingevent.ui.upcoming

import android.content.Context
//import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.dicodingevent.ListEventAdapter
//import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.ui.detail.DetailActivity
import com.dicoding.dicodingevent.databinding.FragmentUpcomingBinding
//import com.dicoding.dicodingevent.ui.finished.FinishedViewModel

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UpcomingViewModel
    private val listEventAdapter = ListEventAdapter { eventId ->
        DetailActivity.start(requireContext() , eventId)
    }

    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        setRecyclerView()
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
                viewModel.getEvents()
                searchBar.setText("")
            }
            searchView.hide()
            keyboardHide()
        }
    }



//    private fun setRView() {
//        binding.rvListEvent.apply {
//            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//            adapter = listEventAdapter
//        }
//    }

    private fun setRecyclerView(){
        binding.rvListUpcoming.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = listEventAdapter

        }
    }
    private fun setViewModel() {
        viewModel = ViewModelProvider(this)[UpcomingViewModel::class.java]

        viewModel.listEvent.observe(viewLifecycleOwner) { event ->
            listEventAdapter.submitList(event)

        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext() , errorMessage , Toast.LENGTH_SHORT).show()
        }
    }

    private fun keyboardHide() {
        val hide =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        hide.hideSoftInputFromWindow(requireView().windowToken , 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}