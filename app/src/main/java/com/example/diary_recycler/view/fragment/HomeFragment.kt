package com.example.diary_recycler.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diary_recycler.*
import com.example.diary_recycler.adapter.SwipeAdapter
import com.example.diary_recycler.dataClass.WriteData
import com.example.diary_recycler.databinding.FragmentHomeBinding
import com.example.diary_recycler.view.activity.WriteActivity

class HomeFragment : Fragment() {
    lateinit var swipeadapter: SwipeAdapter
    val datas = mutableListOf<WriteData>()
    lateinit var helper:SqliteHelper
    //val helper = SqliteHelper(this.context,"article",null,1)

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment


        binding.swipeLayout.setOnRefreshListener {
            initRecycler()
            binding.swipeLayout.setRefreshing(false)
        }
        return binding.root
        }


    private fun initRecycler() {//select
        swipeadapter = SwipeAdapter(requireContext())
        helper = SqliteHelper(getActivity(), "article", null, 1)
        swipeadapter.datas.addAll(helper.selectArticle())//helper의 select값을 swipeadater의 datas에 넣는다.
        swipeadapter.helper = helper//helper 동기화
        binding.rvProfile.adapter = swipeadapter
        binding.rvProfile.apply {
            layoutManager = LinearLayoutManager(context)

        }//initRecycler()//완료 버튼 누르면 데이터 바인딩

        //+버튼 누르면 WriteActivity 시작
        binding.floatingActionButton.setOnClickListener{
            activity?.let{
                Log.e("homeFrag.initRecycler", "1")
                //수정
                val writeActivity =  WriteActivity()
                writeActivity.helper = helper
                val intent = Intent(context, writeActivity::class.java)
                /////////////////////
                startActivity(intent)
                Log.e("homeFrag.initRecycler", "2")
            }
        }

        /*datas.apply {
            add(WriteData(1, "연정", "호롤롤로", System.currentTimeMillis()))

            swipeadapter.datas = datas//put writedata into swipeadater
            Log.e("HomeFragment.applyDatas",swipeadapter.itemCount.toString())
            swipeadapter.notifyDataSetChanged()

        }*/


    }
    fun setArticle(){//insert//select
        var content = arguments?.getString("content")
        var title = arguments?.getString("title")
        var img = arguments?.getString("img")

        if (content != null) {

            val article = title?.let { WriteData(null, it, content, System.currentTimeMillis(), img ) }

            if (article != null) {
                helper.insertArticle(article)
            }//새로 입력한 데이터를 helper에 넣는다.

            swipeadapter.datas.clear()
            swipeadapter.datas.addAll(helper.selectArticle())//swipeadapter 비우고 입력한 값 select 해서 추가
            swipeadapter.notifyDataSetChanged()
            Log.e("HomeFrag.setArticle", "finished")

        }else{
            Log.e("HomeFrag.setArticle", "failed")
        }

    }
    override fun onResume() {
        super.onResume()
        initRecycler()
        Log.e("I'm at HomeFragment", "1")

    }
}
