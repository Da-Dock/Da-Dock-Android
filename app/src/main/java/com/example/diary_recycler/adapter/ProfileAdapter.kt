package com.example.diary_recycler.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diary_recycler.R
import com.example.diary_recycler.dataClass.SwipeData

class ProfileAdapter (private val context: Context) : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    var datas = mutableListOf<SwipeData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_profile,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imgProfile: ImageView = itemView.findViewById(R.id.imageView2)

        fun bind(item: SwipeData) {
            if(item.img==null)
                Glide.with(itemView).load(R.drawable.placeholder).into(imgProfile)
            else Glide.with(itemView).load(item.img).centerCrop().into(imgProfile)

        }
    }


}