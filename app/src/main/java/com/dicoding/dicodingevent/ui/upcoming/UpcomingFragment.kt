package com.dicoding.dicodingevent.ui.upcoming

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.dicodingevent.R

class UpcomingFragment : Fragment() {

    companion object {
        fun newInstance() = UpcomingFragment()
    }

    private val viewModel: UpcomingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_upcoming , container , false)
    }
}