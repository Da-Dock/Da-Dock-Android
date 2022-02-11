package com.example.diary_recycler

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.diary_recycler.dataClass.WriteData

class SqliteHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        val create =
            "create table article (id integer primary key, title text, content text, datetime integer, img text)"
        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    //insert 메소드
    fun insertArticle(article: WriteData) {
        val values = ContentValues()
        //넘겨줄 컬럼의 매개변수 지정
        values.put("title", article.title)
        values.put("content", article.content)
        values.put("datetime", article.datetime)
        values.put("img", article.img)
        //쓰기나 수정이 가능한 데이터베이스 변수
        val wd = writableDatabase
        wd.insert("article", null, values)
        wd.close()
    }


    //select 메소드
    fun selectArticle(): MutableList<WriteData> {
        val list = mutableListOf<WriteData>()
        val selectAll = "select * from article"
        //읽기전용 데이터베이스 변수
        Log.e("selectArticle text", "1")
        val rd = readableDatabase
        Log.e("selectArticle text", "2")
        //데이터를 받아 줍니다.
        val cursor = rd.rawQuery(selectAll, null)

        //반복문을 사용하여 list 에 데이터를 넘겨 줍시다.
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val title = cursor.getString(cursor.getColumnIndex("title"))
            val content = cursor.getString(cursor.getColumnIndex("content"))
            val datetime = cursor.getLong(cursor.getColumnIndex("datetime"))
            val img = cursor.getString(cursor.getColumnIndex("img"))

            list.add(WriteData(id, title, content, datetime, img))
        }
        cursor.close()
        rd.close()
        //Log.e("datas", list[list.size-1].id.toString())
        return list
    }

    //update 메소드
    fun updateArticle(article: WriteData) {
        val values = ContentValues()

        values.put("title", article.title)
        values.put("content", article.content)
        values.put("datetime", article.datetime)
        values.put("img", article.img)

        val wd = writableDatabase
        wd.update("article", values, "id=${article.id}", null)
        wd.close()

    }

    //delete 메소드
    fun deleteArticle(article: WriteData) {
        val delete = "delete from article where id = ${article.id}"
        val db = writableDatabase
        db.execSQL(delete)
        db.close()

    }
}



