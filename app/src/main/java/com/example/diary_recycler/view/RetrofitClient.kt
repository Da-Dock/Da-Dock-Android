package com.example.diary_recycler.view

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


public class RetrofitClient {
    companion object {
        private var retrofit: Retrofit? = null
        //통신할 서버 url
        private val url = "http://ec2-44-201-147-197.compute-1.amazonaws.com:3000/"
        fun getClient(): Retrofit? {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(url) // 요청을 보낼 base url을 설정한다.
                    .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 GsonConverterFactory를 추가한다.
                    .build()
            }
            return retrofit
        }
    }



}