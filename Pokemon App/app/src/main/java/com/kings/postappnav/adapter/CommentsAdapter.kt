package com.kings.postappnav.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kings.postappnav.R
import com.kings.postappnav.data.CommentsItem
import kotlinx.android.synthetic.main.comments_sample.view.*

class CommentsAdapter(): RecyclerView.Adapter<CommentsAdapter.MyViewHolder>() {

    var commentsItem: MutableList<CommentsItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comments_sample, parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  commentsItem?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val comments = commentsItem?.get(position)
        if (comments != null) {
            holder.setData(comments, position, comments.postId)
        }
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun setData(comments: CommentsItem, position: Int, id: Int) {
            itemView.comments_body_sample.text = comments.body.capitalize()
            itemView.name_comment.text = comments.name.capitalize()
            itemView.email_comment.text = comments.email
        }
    }

    fun setupComment(comments: List<CommentsItem>){
        this.commentsItem = comments as MutableList<CommentsItem>
    }

    fun updatePost (list: MutableList<CommentsItem>) {
        commentsItem = list
        notifyDataSetChanged()
    }
}