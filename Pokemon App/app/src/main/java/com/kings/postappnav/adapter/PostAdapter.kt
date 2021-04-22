package com.kings.postappnav.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kings.postappnav.R
import com.kings.postappnav.data.PostsItem
import com.kings.postappnav.data.UsersItem
import com.kings.postappnav.util.RecyclerViewClickListener
import kotlinx.android.synthetic.main.list_comments_sample.view.*

class PostAdapter: RecyclerView.Adapter<PostAdapter.MyViewHolder>() {

    var postsItem  = mutableListOf<PostsItem>()
    var listener: RecyclerViewClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_comments_sample, parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postsItem.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.post_post.text = postsItem[position].body.capitalize()
        holder.itemView.post_title.text = postsItem[position].title.capitalize()
        holder.itemView.user_name.text = postsItem[position].userId.toString()
        holder.itemView.user_image.setImageResource(R.drawable.avatar_male_three)
        holder.itemView.post_post.setOnClickListener{
            listener?.onRecyclerViewItemClicked(it, postsItem[position], postsItem[position].id)
        }
    }

    class MyViewHolder(itemview: View): RecyclerView.ViewHolder(itemview) {}
//
    fun setupPost(post : List<PostsItem>){
        this.postsItem = post as MutableList<PostsItem>
    }

}