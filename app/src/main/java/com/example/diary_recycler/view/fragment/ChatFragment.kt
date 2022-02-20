package com.example.diary_recycler.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diary_recycler.ServerInterface
import com.example.diary_recycler.adapter.ChatListAdapter
import com.example.diary_recycler.dataClass.ChatListData
import com.example.diary_recycler.dataClass.ChatListResponse
import com.example.diary_recycler.databinding.FragmentChatBinding
import com.example.diary_recycler.view.RetrofitClient


class ChatFragment: Fragment() {
    lateinit var adapter: ChatListAdapter
    var datas = mutableListOf<ChatListData>()
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
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        selectChatRoomList()
        Log.e("I'm at HomeFragment", "1")

    }


    private fun initRecycler() {
        adapter = ChatListAdapter(requireContext())
        adapter.datas.addAll(datas)//어댑터에 데이터 넣기
        binding.rvProfile.adapter = adapter
        adapter.notifyDataSetChanged()

        binding.rvProfile.apply {
            layoutManager = LinearLayoutManager(context)
        }//얘의 위치를 onCreateView로 옮겨볼까?


        /*
        swipeadapter.datas.addAll(datas)//helper의 select값을 swipeadater의 datas에 넣는다.
        // swipeadapter.helper = helper//helper 동기화
        binding.rvProfile.adapter = swipeadapter
        swipeadapter.notifyDataSetChanged()
        val layoutManager = LinearLayoutManager(context)
        layoutManager.reverseLayout =true
        layoutManager.stackFromEnd= true
        binding.rvProfile.layoutManager = layoutManager*/
    }

    fun selectChatRoomList(){
        val retrofit1 = RetrofitClient.getClient()
        var server = retrofit1?.create(ServerInterface::class.java)

        val preferences = requireActivity().getPreferences(0)
        preferences.getString("token", "")?.let { Log.e("token Chat: ", it) }
        val keys: Map<String, *> = preferences.getAll()
        for ((key, value) in keys) {
            Log.d(
                "map values2", key + ": " +
                        value.toString()
            )
        }

        server?.getChatRoomList("토큰")?.enqueue((object: retrofit2.Callback<ChatListResponse> {
            override fun onFailure(call: retrofit2.Call<ChatListResponse>, t: Throwable?) {
                Log.e("ChatListFragment", "가져오기 실패")
            }
            override fun onResponse(call: retrofit2.Call<ChatListResponse>, response: retrofit2.Response<ChatListResponse>){
                if (response.isSuccessful()) {
                    val post: ChatListResponse? = response.body()
                    val flag = post?.code
                    if (flag == 200) {
                        datas= post?.data
                        Log.e("ChatList", "가져오기 성공" + datas.size)
                        Log.e("ChatList", "가져오기 성공" + datas[0].title)
                        initRecycler()
                    } else Log.e("ChatList", "알수 없는 에러",)
                } else Log.e("post", response.toString())
            }
        }))
    }
}
