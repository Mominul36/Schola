package com.example.schola.Adapter


import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.schola.Model.Message
import com.example.schola.R


class MessageAdapter(
    private val context: Context,
    private val myId: String,
    private val messageList: List<Message>
) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var left: TextView = itemView.findViewById(R.id.left)
        var right: TextView = itemView.findViewById(R.id.right)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]

        if(message.senderId==myId){
            holder.left.visibility = View.GONE
            holder.right.visibility = View.VISIBLE
            holder.right.text = message.message
        }else{
            holder.right.visibility = View.GONE
            holder.left.visibility = View.VISIBLE
            holder.left.text = message.message
        }


    }

    override fun getItemCount() = messageList.size



}
