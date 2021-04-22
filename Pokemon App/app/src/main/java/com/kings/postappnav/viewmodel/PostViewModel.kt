package com.kings.postappnav.viewmodel

import androidx.lifecycle.ViewModel
import com.kings.postappnav.data.CommentsItem
import com.kings.postappnav.data.PostsItem
import com.kings.postappnav.repository.PostRepository

class PostViewModel: ViewModel() {

    private var postRepository: PostRepository =
        PostRepository()

    var postList = postRepository.postList
    var commentList = postRepository.commentList
    var likes = postRepository.likes

    //gets post list from repository
    init {
        postRepository.getPost()
    }
    //gets comments list form repository
    fun getComments(id:Int){
        postRepository.getComments(id)
    }

    //updates post list form repository
    fun updatePost(post: PostsItem) {
        postRepository.updatePost(post)
    }

    //updates comments list form repository
    fun updateComment(comment: CommentsItem) {
        postRepository.updateComments(comment)
    }
}