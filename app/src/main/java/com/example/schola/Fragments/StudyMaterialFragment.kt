package com.example.schola.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.schola.databinding.FragmentStudyMaterialBinding
import com.google.firebase.database.FirebaseDatabase

class StudyMaterialFragment : Fragment() {

    lateinit var binding: FragmentStudyMaterialBinding
    lateinit var assignCourseId: String
    private var driveLink: String? = null // To store the fetched drive link

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyMaterialBinding.inflate(inflater, container, false)
        assignCourseId = arguments?.getString("assignCourseId") ?: ""

        // Set up WebView settings
        val webSettings: WebSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        binding.webView.webViewClient = WebViewClient()

        fetchDriveLink()

        // Open the browser with the fetched drive link
        binding.go.setOnClickListener {
            if (!driveLink.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(driveLink))
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Drive link is not available", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun fetchDriveLink() {
        val database = FirebaseDatabase.getInstance().getReference("StudyMaterial")

        database.child(assignCourseId).get().addOnSuccessListener {
            if (it.exists()) {
                driveLink = it.child("driveLink").value.toString() // Store the drive link
                binding.webView.loadUrl(driveLink!!)
            } else {
                Toast.makeText(requireContext(), "Drive is not set", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}
