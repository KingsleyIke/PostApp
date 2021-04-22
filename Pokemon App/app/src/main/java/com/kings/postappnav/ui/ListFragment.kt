package com.kings.postappnav.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.kings.postappnav.adapter.PostAdapter
import com.kings.postappnav.R
import com.kings.postappnav.data.PostsItem
import com.kings.postappnav.data.UsersItem
import com.kings.postappnav.databinding.FragmentListBinding
import com.kings.postappnav.util.RecyclerViewClickListener
import com.kings.postappnav.viewmodel.PostViewModel
import kotlinx.android.synthetic.main.add_post.view.*

class ListFragment : Fragment(), RecyclerViewClickListener {

    private var binding: FragmentListBinding? = null
    lateinit var viewModel: PostViewModel
    lateinit var postAdapter: PostAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)

        //calling Methods to be implemented
        initRecyclerView()
        loadApiData()

        postAdapter.listener = this

        //Onclick listener to call alert dialog
        binding!!.addButton.setOnClickListener {
            alertDialog()
        }

        //Text change listner to observe search items
        binding!!.searchPost.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                search(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return binding!!.root
    }

    /**
     * Initializing recycler view
     * setting up adapter
     */
    private fun initRecyclerView(){
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            postAdapter = PostAdapter()
            adapter = postAdapter
        }
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
     * observes postlist from viewmodel
     * sets the data from the api call to update the adapter
     */
    fun loadApiData() {
        viewModel.postList.observe(viewLifecycleOwner, Observer<List<PostsItem>> {
            if (it != null) {
                postAdapter.setupPost(it)
                postAdapter.notifyDataSetChanged()
            }
        })
    }

    /**
     * Implements onRecyclerViewItemClicked
     * Sets Up sliding in and out animation
     * Onclick opens up comments fragemnt using navigation action
     */
    override fun onRecyclerViewItemClicked(view: View, post: PostsItem, id : Int) {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_in_right
            }
        }
        when (view.id) {
            R.id.post_post -> {
                val action = ListFragmentDirections.actionListFragmentToCommentFragment(id)
                findNavController().navigate(action, options)
            }
        }
    }

    /**
     * observes postList from viewmodel
     * takes in post object as param
     * adds post object to comments list
     */
    fun addPost(post: PostsItem){
        viewModel.updatePost(post)
        viewModel.postList.observe(viewLifecycleOwner, Observer<List<PostsItem>>{
            if(it != null) {
                postAdapter.setupPost(it)
                postAdapter.notifyDataSetChanged()
            }
            else {
                Toast.makeText(this.context, "Error in Updating data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Observes postlist from viewmodel
     * sets search item to new list and updates adapter
     */
    fun search (searchItem: String) {
        viewModel.postList.observe(this, Observer<List<PostsItem>>{
            val tempList: MutableList<PostsItem> = ArrayList()
            for (data in it) {
                if (searchItem in data.body || searchItem in data.title) {
                    tempList.add(data)
                }
            }
            binding?.searchCount?.text = tempList.size.toString()
            postAdapter.setupPost(tempList)
            postAdapter.notifyDataSetChanged()
        })
    }

    /**
     *Creates an alert dialog layout
     * calls the addcomments function
     * passes in generated comments object to addcomments method
     * dismiss alert dialog
     */
    fun alertDialog () {
        val dialog = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.add_post,null)
        dialog.setView(view)
        val postDialog =  dialog.create()
        postDialog.show()

        view.add_new_post_button.setOnClickListener {
            val post = (PostsItem(view.add_new_post.text.toString(),101, view.add_new_title.text.toString(), 101))
            addPost(post)
            postDialog.dismiss()
        }

        view.cancel_action_post_button.setOnClickListener {
            postDialog.dismiss()
        }
    }
}