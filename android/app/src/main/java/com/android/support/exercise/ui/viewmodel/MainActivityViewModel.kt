package com.android.support.exercise.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.support.exercise.bd.AppDatabase
import com.android.support.exercise.data.Post
import com.android.support.exercise.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.util.Log


class MainActivityViewModel : ViewModel() {

    private val _userName: MutableLiveData<String?> = MutableLiveData("Loading name...")
    val userName: LiveData<String?> get() = _userName

    private val _userImage: MutableLiveData<String?> =
        MutableLiveData("https://cdn-icons-png.flaticon.com/512/9815/9815472.png")
    val userImage: LiveData<String?> get() = _userImage

    private val _posts: MutableLiveData<ArrayList<Post>> =
        MutableLiveData(arrayListOf())
    val posts: LiveData<ArrayList<Post>> get() = _posts


    fun loadPostsData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val posts = RetrofitClient.apiService.getPosts()
                Log.d("MainActivityViewModel", "Posts fetched: ${posts.size}")
                CoroutineScope(Dispatchers.Main).launch {
                    _posts.value = posts.toCollection(arrayListOf())
                }
            } catch (e: Exception) {
                Log.e("MainActivityViewModel", "Error loading posts", e)
                e.printStackTrace()
            }
        }
    }

    //Wrapped the database operations in CoroutineScope(Dispatchers.IO).launch to ensure they run on a background thread.
    //You are calling AppDatabase.getDatabase(context)?.userDao()?.getAllUsers() directly in the loadUserData method, which runs on the main thread.
    //Fix:
    //Move the database operations to a background thread using Kotlin coroutines. You are already using coroutines for loadPostsData, so you can apply the same approach to loadUserData.
    fun loadUserData(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = AppDatabase.getDatabase(context)
                    ?.userDao()
                    ?.getAllUsers()?.firstOrNull()

                CoroutineScope(Dispatchers.Main).launch {
                    user?.let {
                        _userName.value = it.name
                        _userImage.value = it.image
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}