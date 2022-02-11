package com.example.diary_recycler.adapter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diary_recycler.dataClass.ChatModel
import com.example.diary_recycler.R


class ChatAdapter (private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val chatContent= mutableListOf<ChatModel>()
    internal lateinit var preferences: SharedPreferences
    //lateinit var userName: String
    //lateinit var thisName: String

    fun addItem(item: ChatModel) {//아이템 추가
        if (chatContent != null) {
            chatContent.add(item)
            Log.e("ChatAdapter additem", "success")
        }
    }

    //var datas = mutableListOf<SwipeData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View//LayoutInflater.from(context).inflate(R.layout.item_chat,parent,false)

        if (viewType == 1){//내쪽 말풍선
            view = LayoutInflater.from(context).inflate(R.layout.my_chat,parent,false)
            return Holder(view)
        }else{//반대쪽 말풍선
            view = LayoutInflater.from(context).inflate(R.layout.your_chat,parent,false)
            return Holder2(view)
        }

    }

    override fun getItemCount(): Int {
        return chatContent.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is Holder){
            (holder as Holder).chat_Text?.setText(chatContent.get(position).script)
            (holder as Holder).chat_Time?.setText(chatContent.get(position).date_time)
        }
        else if(holder is Holder2) {
            (holder as Holder2).chat_You_Image?.setImageResource(R.mipmap.ic_launcher)
            (holder as Holder2).chat_You_Name?.setText(chatContent.get(position).name)
            (holder as Holder2).chat_Text?.setText(chatContent.get(position).script)
            (holder as Holder2).chat_Time?.setText(chatContent.get(position).date_time)
        }
        /*holder.itemView.setOnClickListener{

            val intent = Intent(holder.itemView?.context, ChatActivity::class.java)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }*/
  }

    /*inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        /*private val txtName: TextView = itemView.findViewById(R.id.tv_rv_name)
        private val txtAge: TextView = itemView.findViewById(R.id.tv_rv_age)
        private val imgProfile: ImageView = itemView.findViewById(R.id.img_rv_photo)*/

        fun bind(item: SwipeData) {
            /*txtName.text = item.name
            txtAge.text = item.age.toString()
            if(item.img==null)
                Glide.with(itemView).load(R.drawable.placeholder).into(imgProfile)
            else Glide.with(itemView).load(item.img).centerCrop().into(imgProfile)*/
        }
    }*/




    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chat_Text:TextView = itemView.findViewById(R.id.chat_Text)
        val chat_Time:TextView = itemView.findViewById(R.id.chat_Time)
    }

    inner class Holder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chat_You_Image:ImageView = itemView?.findViewById(R.id.chat_You_Image)
        val chat_You_Name:TextView = itemView?.findViewById(R.id.chat_You_Name)
        val chat_Text:TextView = itemView?.findViewById(R.id.chat_Text)
        val chat_Time:TextView = itemView?.findViewById(R.id.chat_Time)//binding으로 대체 가능?
    }

    override fun getItemViewType(position: Int): Int {//여기서 뷰타입을 1, 2로 바꿔서 지정해줘야 내채팅 너채팅을 바꾸면서 쌓을 수 있음
        preferences = context.getSharedPreferences("USERSIGN", Context.MODE_PRIVATE)

        //내 아이디와 arraylist의 name이 같다면 내꺼 아니면 상대꺼
        return if (chatContent.get(position).name == preferences.getString("name", "")) {//나
            1
        } else {//너
            2
        }
    }


}