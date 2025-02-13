package com.dicoding.dicodingevent.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import com.dicoding.dicodingevent.R
import android.view.View
//import android.widget.ImageView
//import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
//import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
//import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.databinding.ActivityDetailBinding
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val eventId = intent.getStringExtra(EXTRA_EVENT_ID)
        val eventName = intent.getStringExtra("EXTRA_EVENT_NAME")
        val eventSummary = intent.getStringExtra("EXTRA_EVENT_SUMMARY")
        val eventImage = intent.getStringExtra("EXTRA_EVENT_IMAGE")

        setInitialEventData(eventName, eventSummary, eventImage)

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        eventId?.let { viewModel.getDetailEvents(it) }

        setEventData()
        observeViewModel()
    }

    private fun setInitialEventData(eventName: String?, eventSummary: String?, eventImage: String?) {
        with(binding) {
            tvTitle.text = eventName ?: "Nama tidak tersedia"
            tvEventDescription.text = eventSummary ?: "Deskripsi tidak tersedia"

            Glide.with(this@DetailActivity)
                .load(eventImage)
                .placeholder(R.drawable.placeholder_image) // Placeholder saat gambar loading
                .error(R.drawable.error_image) // Jika gambar gagal dimuat
                .into(ivImageLogo)
        }
    }

    private fun observeViewModel() {
        viewModel.detailEvent.observe(this) { event ->
            event?.let {
                val totalQuota = it.quota - it.registrants
                with(binding) {
                    tvOwnerName.text = it.ownerName
                    tvQuota.text = totalQuota.toString()
                    tvBeginTime.text = dateFormat(it.beginTime, it.endTime)
                    tvEventDescription.text = HtmlCompat.fromHtml(it.description, HtmlCompat.FROM_HTML_MODE_LEGACY)

                    // Update gambar dengan data dari API
                    Glide.with(this@DetailActivity)
                        .load(it.mediaCover)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(ivImageLogo)
                }
            }
        }

        viewModel.isLoading.observe(this) { showLoading(it) }
        viewModel.errorMessage.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setEventData() {
        viewModel.detailEvent.observe(this) { event ->

            val totalQuota = event.quota - event.registrants
            with(binding) {
                //tvCategory.text = event.category
                tvOwnerName.text = event.ownerName
                tvEventDescription.text = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
                tvTitle.text = event.name
                tvQuota.text = totalQuota.toString()
                //tvDetCityName.text = event.cityName
                tvBeginTime.text = dateFormat(event.beginTime, event.endTime)
                Glide.with(this@DetailActivity)
                    .load(event.mediaCover)
                    .into(binding.ivImageLogo)

                btnRegister.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                    startActivity(intent)
                }

            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun dateFormat(beginTime: String, endTime: String): String {
        val input = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val output = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US)
        try {
            val beginDate = input.parse(beginTime)
            val endDate = input.parse(endTime)
            return "${beginDate?.let { output.format(it) }} - ${endDate?.let {
                output.format(
                    it
                )
            }}"
        } catch (e: ParseException) {
            e.printStackTrace()
            return "$beginTime - $endTime"
        }
    }

    companion object {
        private const val EXTRA_EVENT_ID = "extra_event_id"

        fun start(context: Context, eventId: String) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_EVENT_ID, eventId)
            context.startActivity(intent)
        }
    }
}
