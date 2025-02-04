package com.dicoding.dicodingevent.ui.finished

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
//import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.ListEventAdapter
import com.dicoding.dicodingevent.databinding.FragmentFinishedBinding
import com.dicoding.dicodingevent.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var finishedViewModel: FinishedViewModel

    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater , container , false)
        val root: View = binding.root

        finishedViewModel = ViewModelProvider(this).get(FinishedViewModel::class.java)

        binding.rvEvents.layoutManager = LinearLayoutManager(context)

        finishedViewModel.getEvent().observe(viewLifecycleOwner){ eventList ->
            binding.progressBar.visibility = View.GONE

            if(eventList != null && eventList.isNotEmpty()){
                val adapter = ListEventAdapter(eventList) { event ->
                    val intent = Intent(requireContext() , DetailActivity::class.java).apply {
                        putExtra("id" , event.id)
                    }
                    startActivity(intent)
                }
                binding.rvEvents.adapter = adapter
                binding.emptyMessage.visibility = View.GONE

                } else {
                    binding.emptyMessage.visibility = View.VISIBLE
                    binding.rvEvents.adapter = null
            }
        }

        finishedViewModel.getErrorMessage().observe(viewLifecycleOwner){ errorMessage ->
            if (errorMessage != null && errorMessage.isNotEmpty()) {
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
            }
        }

        if(finishedViewModel.getEvent().value == null) {
            binding.progressBar.visibility = View.VISIBLE
            finishedViewModel.fetchFinished()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(!query.isNullOrEmpty()){
                    binding.progressBar.visibility = View.VISIBLE
                    finishedViewModel.searchEvents(query)
                 }

                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrEmpty()){
                    finishedViewModel.fetchFinished()
                }
                return false
            }


        })






       // val textView: TextView = binding.textFinished
//        finishedViewModel.text.observe(viewLifecycleOwner) {
//            //textView.text = it
//        }
        return root
    }

    override fun onResume() {
        super.onResume()
        finishedViewModel.fetchFinished(forceReload = true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}