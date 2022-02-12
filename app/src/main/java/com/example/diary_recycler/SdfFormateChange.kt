package com.example.diary_recycler

import java.text.SimpleDateFormat
import java.util.*

class SdfFormateChange(sdf: SimpleDateFormat) {
    companion object{
        fun makeSdfTime(sdf: SimpleDateFormat): String{
            val now = System.currentTimeMillis()
            val date = Date(now)

            return(sdf.format(date))
        }

    }
}