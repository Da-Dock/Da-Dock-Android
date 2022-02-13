package com.example.diary_recycler.dataClass

import retrofit2.http.Field

class PostData(
    val postId: String,
    val userId: String,
    val title: String,
    val content: String,
    val contentImg: String,
    val created: String,
    val updated: String,
    val code: Int
) {}//리스트로 받아와볼까..?
/*@Field("userId") userId:String,
@Field("title") title:String,
@Field("content") content:String,
@Field("contentImg") contentImg:String,
@Field("created") created:String)*/