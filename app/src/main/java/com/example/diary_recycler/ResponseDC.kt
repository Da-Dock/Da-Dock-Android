package com.example.diary_recycler

import com.example.diary_recycler.dataClass.Login
import com.example.diary_recycler.dataClass.SignUp
import retrofit2.Call
import retrofit2.http.*


data class ResponseDC(var result:String? = null)

interface ServerInterface {
    @GET("/main")
    fun getRequest(@Query("name") name: String): Call<ResponseDC>

    @GET("/post")
    fun getPostRequest(@Field("userId") userId:String,
                       @Field("title") title:String,
                       @Field("content") content:String,
                       @Field("contentImg") contentImg:String,
                       @Field("created") created:String): Call<ResponseDC>

    @FormUrlEncoded
    @POST("/join/login")
    fun loginRequest(@Field("userToken") userToken:String,
                    @Field("userEmail") userEmail:String,
                    @Field("userName") userName:String,
                    @Field("profileImg") profileImg:String):Call<SignUp>

    @FormUrlEncoded
    @POST("/post/post")
    fun postRequest(@Field("userId") userId:String,
                    @Field("title") title:String,
                    @Field("content") content:String,
                    @Field("contentImg") contentImg:String,
                    @Field("created") created:String):Call<ResponseDC>

    @FormUrlEncoded
    @PUT("/{id}")
    fun putRequest(@Path("id")id: String,
                   @Field("content")content: String): Call<ResponseDC>

    @DELETE("/{id}")
    fun deleteRequest(@Path("id")id: String): Call<ResponseDC>


    @FormUrlEncoded
    @POST("/join/login")
    fun postSignUp(@FieldMap param: HashMap<String?, Any?>?): Call<SignUp>
}