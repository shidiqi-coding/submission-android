package com.dicoding.dicodingevent.ui.detail

//import androidx.fragment.app.viewModels
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

//    companion object {
//        fun newInstance() = DetailActivity()
//    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    //private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra("id",0)
        Log.d("DetailActivity", "Event ID : $eventId")


        if(eventId != 0) {
            viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
            viewModel.fetchEventDetail(eventId.toString())

        } else {
            Log.e("DetailActivity", "Invalid Event ID")
        }

        viewModel.event.observe(this) { event ->
            if(event != null){
                binding.tvTitle.text = event.name ?:"No Title"
                binding.tvOwnerName.text= "Owner Name: ${event.ownerName ?: "N/A"}"
                binding.tvBeginTime.text = "Time: ${event.beginTime ?:"N/A"}"
                binding.tvQuota.text="Remaining Quota ${(event.quota ?:0) - (event.registrants?:0)}"
                binding.tvEventDescription.text = event.description ?: "No Description"
                Glide.with(this).load(event.mediaCover).into(binding.ivImageLogo)


                binding.btnRegister.setOnClickListener {
                    val eventLink = event.link ?: "https://defaultlink.com"
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(eventLink)))

                }


            } else{
                Log.e("DetailActivity", "Event data is null")
            }

        }
    }

//    override fun onCreateView(
//        inflater: LayoutInflater , container: ViewGroup? ,
//        savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(R.layout.activity_detail , container , false)
//    }
}