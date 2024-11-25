package com.example.schola.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.schola.MessageActivity
import com.example.schola.Model.User
import com.example.schola.Model.User2
import com.example.schola.R

class User2Adapter(
    private val context: Context,
    private val users: List<User2>
) : RecyclerView.Adapter<User2Adapter.User2ViewHolder>() {

    class User2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pic: ImageView = itemView.findViewById(R.id.pic)
        val name: TextView = itemView.findViewById(R.id.name)
        val lastMessage: TextView = itemView.findViewById(R.id.lastMessage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): User2ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_user, parent, false)
        return User2ViewHolder(view)
    }

    override fun onBindViewHolder(holder: User2ViewHolder, position: Int) {
        val user = users[position]
         holder.name.text = user.Name
        holder.lastMessage.text = user.lastMessage
        // Load image using Glide or Picasso for userPic

        if(user.pic==""){
            Glide.with(holder.pic.context)
                .load(R.drawable.profile)
                .into(holder.pic)
        }else{
            Glide.with(holder.pic.context)
                .load(user.pic)
                .into(holder.pic)
        }

        holder.itemView.setOnClickListener{
            var intent = Intent(context,MessageActivity::class.java)
            intent.putExtra("opId",user.id)
            context.startActivity(intent)

        }


    }

    override fun getItemCount(): Int = users.size
}
