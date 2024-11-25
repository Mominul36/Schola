package com.example.schola.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.schola.Model.LabReportItem
import com.example.schola.R

class LabReportAdapter(
    private val labReportList: List<LabReportItem>
) : RecyclerView.Adapter<LabReportAdapter.LabReportViewHolder>() {

    inner class LabReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseCode: TextView = itemView.findViewById(R.id.courseCode)
        val lrNo: TextView = itemView.findViewById(R.id.lrNo)
        val date: TextView = itemView.findViewById(R.id.date)
        val topic: TextView = itemView.findViewById(R.id.topic)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabReportViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_lab_report, parent, false)
        return LabReportViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LabReportViewHolder, position: Int) {

        val labReport = labReportList[position]

        holder.courseCode.text = labReport.courseCode ?: "N/A"
        holder.lrNo.text = labReport.lrNo ?: "N/A"
        holder.date.text = labReport.date ?: "N/A"
        holder.topic.text = labReport.topic ?: "N/A"



    }

    override fun getItemCount() = labReportList.size
}
