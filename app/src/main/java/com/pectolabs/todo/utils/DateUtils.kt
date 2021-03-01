package com.pectolabs.todo.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object{
        fun fromDateObjectToString(date: Date):String{
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return sdf.format(date)
        }
    }
}