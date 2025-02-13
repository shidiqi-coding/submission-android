package com.dicoding.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
//import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.dicodingevent.databinding.FragmentHomeBinding
import com.dicoding.dicodingevent.ui.detail.DetailActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private val homeFinishedEventAdapter = HomeFinishedEventAdapter { eventId ->
        DetailActivity.start(requireContext(), eventId.toString())
    }

    private val homeUpcomingEventAdapter = HomeUpcomingEventAdapter { eventId ->
        DetailActivity.start(requireContext(), eventId)
    }

    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater , container , false)
        val root: View = binding.root

        //val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            //textView.text = it
//        }


        return root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        setRvViews()
        setViewModels()

    }

    private fun setRvViews() {
      binding.rvUpcoming.apply {
          layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
          adapter = homeUpcomingEventAdapter
      }

        binding.rvFinished.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            adapter = homeFinishedEventAdapter
        }
    }

    private fun setViewModels(){
         viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        viewModel.upcomingEvents.observe(viewLifecycleOwner) {events ->
            homeUpcomingEventAdapter.submitList(events)
            binding.rvUpcoming.visibility = if (events.isEmpty()) View.GONE else View.VISIBLE
            binding.textViewUpcoming.visibility = if (events.isEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.finishedEvents.observe(viewLifecycleOwner) {events ->
            homeFinishedEventAdapter.submitList(events)
            binding.rvFinished.visibility = if (events.isEmpty()) View.GONE else View.VISIBLE
            binding.textViewFinished.visibility = if (events.isEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
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

    private fun showLoading(isLoading : Boolean) {
        binding.progressBarHome.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}