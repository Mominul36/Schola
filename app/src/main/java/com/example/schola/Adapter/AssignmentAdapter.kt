package com.example.schola.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schola.Model.Assignment
import com.example.schola.Model.AssignmentItem
import com.example.schola.R

class AssignmentAdapter(
    private val AssignmentList: List<AssignmentItem>
) : RecyclerView.Adapter<AssignmentAdapter.AssegnmentViewHolder>() {

    inner class AssegnmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseCode: TextView = itemView.findViewById(R.id.courseCode)
        val assignmentNo: TextView = itemView.findViewById(R.id.assignmentNo)
        val date: TextView = itemView.findViewById(R.id.date)
        val topic: TextView = itemView.findViewById(R.id.topic)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssegnmentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_assignment, parent, false)
        return AssegnmentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AssegnmentViewHolder, position: Int) {

        val assignment = AssignmentList[position]

        holder.courseCode.text = assignment.courseCode
        holder.assignmentNo.text = assignment.assignmentNo
        holder.date.text = assignment.date
        holder.topic.text = assignment.topic



    }

    override fun getItemCount() = AssignmentList.size
}
