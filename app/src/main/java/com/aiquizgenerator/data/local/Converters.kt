package com.aiquizgenerator.data.local
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter fun fromList(v: List<String>): String = Gson().toJson(v)
    @TypeConverter fun toList(v: String): List<String> = Gson().fromJson(v, object : TypeToken<List<String>>() {}.type)
}
