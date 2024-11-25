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
import com.example.schola.R

class UserAdapter(
    private val context: Context,
    private val users: List<User>
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userPic: ImageView = itemView.findViewById(R.id.pic)
        val userName: TextView = itemView.findViewById(R.id.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.userName.text = user.Name
        // Load image using Glide or Picasso for userPic

        if(user.pic==""){
            Glide.with(holder.userPic.context)
                .load(R.drawable.profile)
                .into(holder.userPic)
        }else{
            Glide.with(holder.userPic.context)
                .load(user.pic)
                .into(holder.userPic)
        }

        holder.itemView.setOnClickListener{
            var intent = Intent(context,MessageActivity::class.java)
            intent.putExtra("opId",user.id)
            context.startActivity(intent)
            if (context is Activity) {
                (context as Activity).finish()
            }
        }


    }

    override fun getItemCount(): Int = users.size
}
