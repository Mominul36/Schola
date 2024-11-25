package com.example.schola.Adapter

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.schola.Model.Question
import com.example.schola.R
import java.util.*

class QuestionAdapter(
    private val context: Context,
    private val questionList: List<Question>
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    private var downloadId: Long = -1

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.image)
        var download: ImageView = itemView.findViewById(R.id.download)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questionList[position]

        // Load the image using Glide
        Glide.with(context)
            .load(question.questionLink.toString())
            .into(holder.image)

        // Set the click listener for the download button
        holder.download.setOnClickListener {
            // Call the function to download the image
            downloadImage(question.questionLink.toString())
        }
    }

    override fun getItemCount() = questionList.size

    // Function to download the image using DownloadManager
    private fun downloadImage(imageUrl: String) {
        // Check for permission to write to external storage (for Android versions < 10)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                // Request permission to write to external storage if not granted
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                return
            }
        }

        // Prepare the download request
        val request = DownloadManager.Request(Uri.parse(imageUrl))
            .setTitle("Downloading Image")
            .setDescription("Downloading the selected image")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "image_${System.currentTimeMillis()}.jpg")

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadId = downloadManager.enqueue(request)

        // Register a receiver to listen for the download completion
        context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(ctx: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    Toast.makeText(context, "Download completed successfully!", Toast.LENGTH_LONG).show()
                }
            }
        }, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show()
    }
}
