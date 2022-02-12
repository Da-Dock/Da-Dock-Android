package com.example.diary_recycler.dataClass

class PostResponse(val data: MutableList<PostData>, val code: Int,
                   val msg: String) {}