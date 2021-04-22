package com.kings.postappnav.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kings.postappnav.data.CommentsItem
import com.kings.postappnav.data.PostsItem
import com.kings.postappnav.data.UsersItem
import com.kings.postappnav.network.PostApi
import com.kings.postappnav.network.RetrofitClient
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PostRepository {
    private var _postList = MutableLiveData<ArrayList<PostsItem>>()
    val postList: LiveData<ArrayList<PostsItem>> = _postList

    private var _commentList: MutableLiveData<ArrayList<CommentsItem>> = MutableLiveData()
    val commentList: LiveData<ArrayList<CommentsItem>> = _commentList

    var likes = MutableLiveData<Int>()

    var retrofitInstance = RetrofitClient.getRetroInstance().create(PostApi::class.java)

    /**
     * Network call to get post data and sets it to postlist
     * subsribing on backgroud thread and observing on main thread
     */
    fun getPost (): MutableLiveData<ArrayList<PostsItem>> {
        retrofitInstance.getPost()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Observer<ArrayList<PostsItem>>{
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {
                    Log.e("Error Message", "$e")
                }
                override fun onNext(t: ArrayList<PostsItem>) {
                    _postList.value = t
                }
            })
        return _postList
    }

    /**
     * Network call to get comments data and sets it to comments list
     * subsribing on backgroud thread and observing on main thread
     */
    fun getComments (id: Int) {
        retrofitInstance.getComments(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Observer<ArrayList<CommentsItem>>{
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {
                    Log.e("ErrorMess getComments", "$e")
                }
                override fun onNext(t: ArrayList<CommentsItem>) {
                    _commentList.value = t
                }
            })
    }

    /**
     * Network call to post call and adds value to postlist
     * subsribing on backgroud thread and observing on main thread
     */
    fun updatePost (post: PostsItem) {
        retrofitInstance.post(post)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Observer<PostsItem>{
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {
                    Log.e("ErrorMessage UpdatePost", "$e")
                }
                override fun onNext(t: PostsItem) {
                    _postList.value?.add(0, t)
                }
            })
    }

    /**
     * Network call to post comments and adds value to commentslist
     * subsribing on backgroud thread and observing on main thread
     */
    fun updateComments (comments: CommentsItem) {
        retrofitInstance.updateComments(comments)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Observer<CommentsItem>{
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {
                    Log.e("ErrorMessage UpComments", "$e")
                }
                override fun onNext(t: CommentsItem) {
                    _commentList.value?.add(0, t)
                }
            })
    }
}