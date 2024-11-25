package com.example.schola

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.schola.Adapter.CourseActivityPagerAdapter
import com.example.schola.databinding.ActivityCourseBinding
import com.google.android.material.tabs.TabLayoutMediator

class CourseActivity : AppCompatActivity() {

    lateinit var binding: ActivityCourseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val assignCourseId = intent.getStringExtra("assignCourseId").toString()

        val adapter = CourseActivityPagerAdapter(this, assignCourseId)
        binding.viewPager.adapter = adapter

        val tabTitles = listOf("Announcement", "Study Material")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()



        var initialFragmentPosition = 0;
        binding.viewPager.setCurrentItem(initialFragmentPosition, false)












        binding.back.setOnClickListener{
            finish()
        }














    }
}