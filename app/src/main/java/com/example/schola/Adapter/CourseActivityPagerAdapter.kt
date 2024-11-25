package com.example.schola.Adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.schola.Fragments.AnnouncementFragment
import com.example.schola.Fragments.StudyMaterialFragment


class CourseActivityPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val assignCourseId: String // Data to pass
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2 // Number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> AnnouncementFragment()
            1 -> StudyMaterialFragment()

            else -> throw IllegalStateException("Invalid position: $position")
        }


        fragment.arguments = Bundle().apply {
            putString("assignCourseId", assignCourseId) // Add any key-value pairs
        }

        return fragment
    }
}

