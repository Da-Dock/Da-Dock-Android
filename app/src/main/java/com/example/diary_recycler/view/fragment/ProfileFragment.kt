package com.example.diary_recycler.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.diary_recycler.adapter.ProfileAdapter
import com.example.diary_recycler.dataClass.SwipeData
import com.example.diary_recycler.databinding.FragmentProfileBinding
import com.example.diary_recycler.view.activity.SettingActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kakao.sdk.user.UserApiClient

class ProfileFragment : Fragment() {
    lateinit var swipeadapter: ProfileAdapter
    val datas = mutableListOf<SwipeData>()
    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(
            layoutInflater
        )
    }
    private var mAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mAuth = FirebaseAuth.getInstance();
        var user: FirebaseUser? = mAuth!!.currentUser


        if(user!=null) {
            binding.nickname.setText(user?.displayName.toString())
            Glide.with(this).load(user?.photoUrl.toString()).into(binding.imgProfile)
        }//구글 프로필 설정

        UserApiClient.instance.me { user, error ->
            if(user!=null) {
                binding.nickname.text = "${user?.kakaoAccount?.profile?.nickname}"
                val uri = "${user?.kakaoAccount?.profile?.thumbnailImageUrl}"
                Glide.with(this).load(uri).into(binding.imgProfile)
            }
        }//카카오 프로필 설정


        initRecycler()
        binding.imageButton.setOnClickListener{
            val settingActivity =  SettingActivity()
            val intent = Intent(context, settingActivity::class.java)
            /////////////////////
            startActivity(intent)
        }
        return binding.root
    }


    private fun initRecycler() {
        swipeadapter = ProfileAdapter(requireContext())
        binding.recyclerView.adapter = swipeadapter
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context,3)

        }

        datas.apply {
            add(SwipeData(img =null, name = "mary", age = "24"))
            add(SwipeData(img =null, name = "jenny", age = "24"))
            add(SwipeData(img =null, name = "jhon", age = "24"))
            add(SwipeData(img =null, name = "ruby", age = "24"))
            add(SwipeData(img =null, name = "yuna", age = "24"))

            swipeadapter.datas = datas
            Log.e("errer",swipeadapter.itemCount.toString())
            swipeadapter.notifyDataSetChanged()

        }
    }
}

