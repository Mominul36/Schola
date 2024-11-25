package com.example.schola

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.schola.databinding.ActivityFacultyBinding

class FacultyActivity : AppCompatActivity() {
    lateinit var binding: ActivityFacultyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFacultyBinding.inflate(layoutInflater)
        setContentView(binding.root)





        binding.web.settings.javaScriptEnabled = true
        binding.web.webViewClient = WebViewClient()



        binding.web.loadUrl(" https://baust.edu.bd/cse/employees/")


        binding.dots.setOnClickListener { view ->
            showMenu(view,  binding.web)
        }








        binding.back.setOnClickListener{
            finish()
        }


    }






    private fun showMenu(view: View, webView: WebView) {
        // Create a PopupMenu
        val popupMenu = PopupMenu(this, view)

        popupMenu.menu.apply {
            add(0, 1, 0, "CSE")
            add(0, 2, 1, "EEE")
            add(0, 3, 2, "ICT")
            add(0, 4, 3, "ME")
            add(0, 5, 4, "CE")
            add(0, 6, 5, "IPE")
            add(0, 7, 6, "DBA")
            add(0, 8, 7, "AIS")
            add(0, 9, 8, "English")
            add(0, 10, 9, "AS")
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            handleMenuClick(menuItem, webView)
            true
        }


        popupMenu.show()
    }

    private fun handleMenuClick(menuItem: MenuItem, webView: WebView) {
        val url = when (menuItem.itemId) {
            1 -> "https://baust.edu.bd/cse/employees/"
            2 -> "https://baust.edu.bd/eee/employees/"
            3 -> "https://baust.edu.bd/ict/employees/"
            4 -> "https://baust.edu.bd/me/employees/"
            5 -> "https://baust.edu.bd/ce/employees/"
            6 -> "https://baust.edu.bd/ipe/employees/"
            7 -> "https://baust.edu.bd/dba/employees/"
            8 -> "https://baust.edu.bd/ais/employees/"
            9 -> "https://baust.edu.bd/eng/employees/"
            10 -> "https://baust.edu.bd/as/employees/"
            else -> null
        }

        // Load the URL in the WebView
        url?.let {
            webView.loadUrl(it)
        }
    }
}