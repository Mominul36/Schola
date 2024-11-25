package com.example.schola.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schola.Model.ClassTest
import com.example.schola.Model.ClassTestItem
import com.example.schola.R

class ClassTestAdapter(
    private val classTestList: List<ClassTestItem>
) : RecyclerView.Adapter<ClassTestAdapter.ClassTestViewHolder>() {

    inner class ClassTestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val courseCode: TextView = itemView.findViewById(R.id.courseCode)
         val ctNo: TextView = itemView.findViewById(R.id.ctNo)
         val time: TextView = itemView.findViewById(R.id.time)
         val date: TextView = itemView.findViewById(R.id.date)
         val topic: TextView = itemView.findViewById(R.id.topic)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassTestViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_class_test, parent, false)
        return ClassTestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClassTestViewHolder, position: Int) {

        val classTest = classTestList[position]

        holder.courseCode.text = classTest.courseCode
        holder.ctNo.text = classTest.ctNo
        holder.time.text = classTest.time
        holder.date.text = classTest.date
        holder.topic.text = classTest.topic



    }

    override fun getItemCount() = classTestList.size
}
