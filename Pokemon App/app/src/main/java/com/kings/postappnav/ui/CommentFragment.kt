package com.kings.postappnav.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.kings.postappnav.R
import com.kings.postappnav.data.CommentsItem
import com.kings.postappnav.data.PostsItem
import com.kings.postappnav.databinding.FragmentCommentBinding
import com.kings.postappnav.adapter.CommentsAdapter
import com.kings.postappnav.viewmodel.PostViewModel
import kotlinx.android.synthetic.main.add_comment.view.*

class CommentFragment (): Fragment() {

    private  var binding: FragmentCommentBinding? = null
    lateinit var viewModel: PostViewModel
    val args : CommentFragmentArgs by navArgs()
    lateinit var commentsAdapter: CommentsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCommentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)

        initRecyclerView()
        updateData(args.id)
        loadComments(args.id)

        binding?.addCommentsButton?.setOnClickListener {
            alertDialog()
        }

        return binding!!.root
    }

    /**
     * Overrides on destroy
     * Sets binding to null to avoid memory leaks
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    /**
     * Initilizing recycler view and setting adapter
     */
    private fun initRecyclerView(){
        binding?.commentRecycler?.apply {
            layoutManager = LinearLayoutManager(context)
            commentsAdapter = CommentsAdapter()
            adapter = commentsAdapter
        }
    }

    /**
     * observes postlist from viewmodel
     * passes data from the the list to new fragment
     * args from previous fragment will be passed in to update data
     */
    private fun updateData (id: Int) {
        viewModel.postList.observe(viewLifecycleOwner, Observer<List<PostsItem>> {
            binding?.titleComments?.text = it[id].title
            binding?.postBody?.text = it[id].body
            binding?.useridComments?.text = it[id].id.toString()
        })
    }

    /**
     * observes commentlist from viewmodel
     * sets the data from the api call to update the adapter
     * updates the number of comments to the size of comments list
     */
    private fun loadComments (id: Int) {
        viewModel.getComments(id)
        viewModel.commentList.observe(viewLifecycleOwner, Observer<List<CommentsItem>> {
            commentsAdapter.setupComment(it)
            commentsAdapter.notifyDataSetChanged()
            binding?.commentsNumber?.text = it.size.toString()
        }
        )
    }

    /**
     * observes commentlist from viewmodel
     * takes in coments object as param
     * adds comment object to comments list
     * updates the number of comments to the size of comments list
     */
    private fun addComment(comment: CommentsItem){
        viewModel.updateComment(comment)
        viewModel.commentList.observe(viewLifecycleOwner, Observer<List<CommentsItem>>{
            if(it != null) {
                commentsAdapter.setupComment(it)
                commentsAdapter.notifyDataSetChanged()
                binding?.commentsNumber?.text = it.size.toString()
            }
            else {
                Toast.makeText(this.context, "Error in Updating data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun likes(){
        viewModel.likes.observe(viewLifecycleOwner, Observer<Int>{
            binding!!.likesNumber.text = it.toString()

        })
    }

    /**
     *Creates an alert dialog layout
     * calls the addcomments function
     * passes in generated comments object to addcomments method
     * dismiss alert dialog
     */

    fun alertDialog() {
        var dialog = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.add_comment, null)
        dialog.setView(view)
                .setTitle("Add Comment")
        val commentDialog = dialog.create()
        commentDialog.show()

        view.add_new_comment_button.setOnClickListener {
            val comment = (CommentsItem(view.add_new_comment.text.toString(), view.comment_email.text.toString(), 1, view.comment_name.text.toString(), 1))
            addComment(comment)
            commentDialog.dismiss()
        }
        view.cancel_action_comment_button.setOnClickListener {
            commentDialog.dismiss()
        }
    }
}