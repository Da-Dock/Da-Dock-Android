package com.example.diary_recycler.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diary_recycler.R
import com.example.diary_recycler.ServerInterface
import com.example.diary_recycler.dataClass.ChatListData
import com.example.diary_recycler.dataClass.ChatListResponse
import com.example.diary_recycler.view.RetrofitClient
import com.example.diary_recycler.view.activity.ChatActivity

class ChatListAdapter (private val context: Context) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {
    var datas = mutableListOf<ChatListData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_swipelist,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size//recyclerview로 가져올 itemview가 몇개?

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data : ChatListData = datas.get(position)
        holder.bind(data)
        //holder.bind(datas[position])

        holder.itemView.setOnClickListener{//채팅방으로 가잣!*********소켓을 새로 생성하지 않게!!!!!
            val intent = Intent(holder.itemView?.context, ChatActivity::class.java)
            //intent.putExtra("id",position)->position으로 datas에 담겨 있는 groupId를 가져와서 보내기?!
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }}

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val groupTitle: TextView = itemView.findViewById(R.id.tv_rv_name)//채팅방 이름
        private val content: TextView = itemView.findViewById(R.id.tv_rv_age)//마지막 채팅 내용
        private val groupImg: ImageView = itemView.findViewById(R.id.img_rv_photo)

        fun bind(item: ChatListData){//서버에서 가져온 db로
            groupTitle.text = item.title
            if(item.contentImg=="")
                Glide.with(itemView).load(R.drawable.placeholder).into(groupImg)
            else Glide.with(itemView).load(item.contentImg).centerCrop().into(groupImg)
            /*txtName.text = item.title
            txtAge.text = item.content
            if(item.contentImg=="")
                Glide.with(itemView).load(R.drawable.placeholder).into(imgProfile)
            else Glide.with(itemView).load(item.contentImg).centerCrop().into(imgProfile)*/
        }

    }
}