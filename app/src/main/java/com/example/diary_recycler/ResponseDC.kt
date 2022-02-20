package com.example.diary_recycler

import com.example.diary_recycler.dataClass.*
import retrofit2.Call
import retrofit2.http.*


data class ResponseDC(var result:String? = null)

interface ServerInterface {
    @GET("/post/postList")//포스트 리스트 가져오기
    fun getPostRequest(@Query("page") page:Int): Call<PostResponse>

    @FormUrlEncoded
    @POST("/post/postInsert")
    fun postRequest2(@Field("userId") userId:String,
                    @Field("title") title:String,
                    @Field("content") content:String,
                    @Field("contentImg") contentImg:String,
                    @Field("created") created:String):Call<PostResponse>

    @FormUrlEncoded//로그인
    @POST("/join/login")
    fun postSignUp(@FieldMap param: HashMap<String?, Any?>?): Call<SignUp>

    @GET("/post/postDetail/{postidx}")//디테일
    fun getdetail(
        @Path("postidx") postidx: String
    ): Call<PostResponse>

    @FormUrlEncoded
    @POST("/chat/chatRoomList_select")//토큰 보내서 채팅리스트 가져오기
    fun getChatRoomList(@Field("token") token:String): Call<ChatListResponse>

    @FormUrlEncoded
    @POST("/chat/chatRoomList_insert")//채팅룸 insert하기
    fun insertChatRoomList(@Field("token") token:String,
                         @Field("groupId") groupId:String,): Call<ChatListResponse>

    //fun getProfile(select용)
    //fun postChatList(insert용)

    //////////////////////////////////////////////////////

    @FormUrlEncoded
    @PUT("/{id}")
    fun putRequest(@Path("id")id: String,
                   @Field("content")content: String): Call<ResponseDC>

    @DELETE("/{id}")
    fun deleteRequest(@Path("id")id: String): Call<ResponseDC>

    /*@FormUrlEncoded
 @POST("/post/post")
 fun postRequest(@FieldMap param: HashMap<String?, Any?>?):Call<PostResponse>

 @FormUrlEncoded
 @POST("/join/login")
 fun loginRequest(@Field("userToken") userToken:String,
                 @Field("userEmail") userEmail:String,
                 @Field("userName") userName:String,
                 @Field("profileImg") profileImg:String):Call<SignUp>

 @GET("/main")
 fun getRequest(@Query("name") name: String): Call<ResponseDC>*/
}