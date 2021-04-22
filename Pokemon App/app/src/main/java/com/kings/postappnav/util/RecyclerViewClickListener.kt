package com.kings.postappnav.util

import android.view.View
import com.kings.postappnav.data.PostsItem

interface RecyclerViewClickListener {

fun onRecyclerViewItemClicked(view: View, post: PostsItem, id : Int)

}