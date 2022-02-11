package com.example.diary_recycler.view.activity

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.diary_recycler.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity(){
    private val binding: ActivitySettingBinding by lazy {
        ActivitySettingBinding.inflate(
            layoutInflater
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        //   supportActionBar!!.setHomeAsUpIndicator(R.drawable.menu)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.navigationIcon?.apply {

            setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN) //뒤로가기 아이콘 색 설정
        }
       binding.check.setOnClickListener{

           val bundle = Bundle()
           bundle.putString("name", binding.nameEt.text.toString())

         /*  if(f!=null) {
               bundle.putString(
                   "img",
                   "https://fofuploadtest.s3.ap-northeast-2.amazonaws.com/" + key //aws s3 이미지 uri
               )
               Log.e("이미지 업로드", "확인"+f.toString())
               uploadImg()
           }
           else */
                bundle.putString("img",null) //이미지 없으면 null로 저장

           finish()
       }
}
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> {
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}