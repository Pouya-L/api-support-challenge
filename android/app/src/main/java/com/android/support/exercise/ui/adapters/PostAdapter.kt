package com.android.support.exercise.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.support.exercise.data.Post

class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var postList: List<Post> = emptyList()

    fun setPosts(posts: List<Post>) {
        postList = posts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.bind(post)
    }

    // There is a critical issue in PostAdapter that is causing the posts not to show up. The problem lies in the getItemCount() method
    // This method is returning 0, which means the RecyclerView thinks there are no items to display.
    // To fix it the getItemCount() method should return the size of the postList:
    override fun getItemCount(): Int = postList.size

    override fun getItemId(position: Int): Long {
        return postList[position].id.toLong()
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(android.R.id.text1)
        private val idView: TextView = itemView.findViewById(android.R.id.text2)

        fun bind(post: Post) {
            titleView.text = "Post: ${post.id}"
            idView.text = post.title
        }
    }
}
