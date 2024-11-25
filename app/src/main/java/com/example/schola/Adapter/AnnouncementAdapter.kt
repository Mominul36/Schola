package com.example.schola.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultRegistry
import androidx.recyclerview.widget.RecyclerView
import com.example.schola.Class.ControlImage
import com.example.schola.Model.Announcement
import com.example.schola.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AnnouncementAdapter(
    private val context: Context,
    private val activityResultRegistry: ActivityResultRegistry,
    private val announcementList: List<Announcement>
) :
    RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>() {

     class AnnouncementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
       var pic: ImageView = itemView.findViewById(R.id.pic)
         var name: TextView = itemView.findViewById(R.id.name)
         var date: TextView = itemView.findViewById(R.id.date)
         var text: TextView = itemView.findViewById(R.id.text)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_announcement, parent, false)
        return AnnouncementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val announcement = announcementList[position]


        val (formattedDate, dayOfWeek) = formatDateAndFindDay(announcement.date.toString())

        holder.name.text = announcement.teacherName
        holder.date.text = formattedDate
        holder.text.text =announcement.text

        if(announcement.teacherPic==""){
            holder.pic.setImageResource(R.drawable.profile)
        }else{
            ControlImage(context, activityResultRegistry,
                "imagePickerKey").setImageByURl(announcement.teacherPic.toString(),holder.pic)
        }


    }

    override fun getItemCount() = announcementList.size


    fun formatDateAndFindDay(inputDate: String): Pair<String, String> {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM, dd", Locale.getDefault())
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())

        val date: Date = inputFormat.parse(inputDate) ?: return Pair("", "")
        val formattedDate = outputFormat.format(date)
        val dayOfWeek = dayFormat.format(date)

        return Pair(formattedDate, dayOfWeek)
    }
}
