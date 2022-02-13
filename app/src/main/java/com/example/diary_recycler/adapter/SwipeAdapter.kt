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
import com.daimajia.swipe.SwipeLayout
import com.example.diary_recycler.Post
import com.example.diary_recycler.R
import com.example.diary_recycler.SqliteHelper
import com.example.diary_recycler.dataClass.PostData
import com.example.diary_recycler.dataClass.WriteData
import com.example.diary_recycler.view.activity.DetailActivity

class SwipeAdapter (private val context: Context) : RecyclerView.Adapter<SwipeAdapter.ViewHolder>() {

    var helper: SqliteHelper? = null
    var datas = mutableListOf<PostData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_swipelist,parent,false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.bind(datas[position])
        val article : PostData = datas.get(position)
        holder.bind(article)


        //writeData 넣을거임이라고 정의
        Log.e("SwipeAdater.bindhold", "I'm here!2")

        val swipelayout :SwipeLayout = holder.itemView.findViewById(R.id.swipe_layout)
        //swipelayout.addSwipeListener(SwipeLayout.SwipeListener)

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView?.context, DetailActivity::class.java)
            intent.putExtra("id",position)
            ContextCompat.startActivity(holder.itemView.context, intent, null)

        }}

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val swipelayout :SwipeLayout = itemView.findViewById(R.id.swipe_layout)
        private val txtName: TextView = itemView.findViewById(R.id.tv_rv_name)
        private val txtAge: TextView = itemView.findViewById(R.id.tv_rv_age)
        private val imgProfile: ImageView = itemView.findViewById(R.id.img_rv_photo)

        /*fun bind(item: WriteData) {
            txtName.text = item.name
            txtAge.text = item.age.toString()
            Glide.with(itemView).load(item.img).into(imgProfile)

        }*/

        fun bind(article: PostData){
            txtName.text = article.title
            txtAge.text = article.content
            Glide.with(itemView).load(article.contentImg).centerCrop().into(imgProfile)
            Log.e("SwipeAdater.setArticle", "I'm here!3 " + txtAge.text+article.contentImg)
        }

    }


}