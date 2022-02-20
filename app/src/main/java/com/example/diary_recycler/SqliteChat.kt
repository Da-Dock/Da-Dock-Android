package com.example.diary_recycler

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.diary_recycler.dataClass.ChatData
import com.example.diary_recycler.dataClass.WriteData

class SqliteChat (context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        val create =
            "create table ChatContent (userId text primary key, groupId text, content text, datetime integer)"//postId 별로 생성?
            //userId에 맞게 db에서 이름이랑 프로필 이미지 가져온다..??????????????????????????????????????대화는 일정 길이 정도만 가져오는것 같은데...
        db?.execSQL(create)
    }//로컬db에 저장할 때는 룸별로 테이블 만들까..????????? 서버는 그냥 chatTable을 만들고? 아아아아아아아아아..!로컬에 저장하고 서버에 백업 하는 시기는 언제로 하지

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertChatContent(chatData: ChatData){
        val values = ContentValues()
        values.put("userId", chatData.userId)
        values.put("groupId", chatData.groupId)
        values.put("content", chatData.content)
        values.put("datetime", chatData.datetime)
        val wd = writableDatabase
        wd.insert("ChatContent", null, values)
        wd.close()
    }

    fun selectChatContent(chatData: ChatData): MutableList<ChatData>{
        val list = mutableListOf<ChatData>()
        val selectChat = "select * from ChatContent"

        val rd = readableDatabase
        val cursor = rd.rawQuery(selectChat, null)

        while (cursor.moveToNext()) {
            val userId = cursor.getString(cursor.getColumnIndex("userId"))
            val groupId = cursor.getString(cursor.getColumnIndex("groupId"))
            val content = cursor.getString(cursor.getColumnIndex("content"))
            val datetime = cursor.getLong(cursor.getColumnIndex("datetime"))

            list.add(ChatData(userId, groupId, content, datetime))
        }
        cursor.close()
        rd.close()
        return list
    }
}