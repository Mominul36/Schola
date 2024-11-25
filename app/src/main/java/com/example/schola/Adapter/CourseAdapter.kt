package com.example.schola.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.schola.CourseActivity
import com.example.schola.Model.CourseItem
import com.example.schola.R
import com.example.schola.databinding.ItemCourseBinding

class CourseAdapter(
    private val context: Context,
    private val courseList: List<CourseItem>
) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]
        with(holder.binding) {
            courseName.text = course.courseName
            courseCode.text = course.courseCode


            Glide.with(icon.context)
                .load(course.icon)
                .placeholder(R.drawable.profile)
                .into(icon)
        }

        holder.itemView.setOnClickListener{

                var intent = Intent(context,CourseActivity::class.java)
                intent.putExtra("assignCourseId",course.assigncourseId)
                context.startActivity(intent)


        }
    }

    override fun getItemCount() = courseList.size
}
