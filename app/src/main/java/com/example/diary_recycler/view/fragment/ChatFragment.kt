package com.example.diary_recycler.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diary_recycler.ChatAdapter
import com.example.diary_recycler.R
import com.example.diary_recycler.SwipeData
import com.example.diary_recycler.databinding.FragmentChatBinding


class ChatFragment: Fragment() {
    lateinit var swipeadapter: ChatAdapter
    val datas = mutableListOf<SwipeData>()
    private val binding: FragmentChatBinding by lazy {
        FragmentChatBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        initRecycler()
        return binding.root
    }


    private fun initRecycler() {
        swipeadapter = ChatAdapter(requireContext())

        binding.rvProfile.adapter = swipeadapter

        binding.rvProfile.apply {
            layoutManager = LinearLayoutManager(context)

        }


        datas.apply {
            add(SwipeData(img = R.drawable.placeholder, name = "mary", age = "24"))
            add(SwipeData(img = R.drawable.placeholder, name = "jenny", age = "24"))
            add(SwipeData(img = R.drawable.placeholder, name = "jhon", age = "24"))
            add(SwipeData(img = R.drawable.placeholder, name = "ruby", age = "24"))
            add(SwipeData(img = R.drawable.placeholder, name = "yuna", age = "24"))

            swipeadapter.datas = datas
            Log.e("errer",swipeadapter.itemCount.toString())
            swipeadapter.notifyDataSetChanged()

        }
    }
}
