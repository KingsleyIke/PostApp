package com.kings.postappnav.network

import com.kings.postappnav.data.CommentsItem
import com.kings.postappnav.data.PostsItem
import com.kings.postappnav.data.UsersItem
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PostApi {

    @GET("posts")
//    fun getPosts(@Query("userId")userId: Int): Observable<List<Posts>>
    fun getPost(): Observable<ArrayList<PostsItem>>

    @GET("comments")
    fun getComments(@Query("postId")id: Int): Observable<ArrayList<CommentsItem>>

    @POST("posts")
    fun post(@Body post: PostsItem): Observable<PostsItem>

    @POST("comments")
    fun updateComments(@Body comments: CommentsItem): Observable<CommentsItem>

    @GET("users")
    fun getUsers(): Observable<ArrayList<UsersItem>>
}