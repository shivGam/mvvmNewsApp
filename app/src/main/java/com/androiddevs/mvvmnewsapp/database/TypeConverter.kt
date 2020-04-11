package com.androiddevs.mvvmnewsapp.database

import androidx.room.TypeConverters
import com.androiddevs.mvvmnewsapp.models.Source

class TypeConverter {
    @TypeConverters
    fun fromSource(source: Source): String{
        return source.name
    }
    @TypeConverters
    fun toSource(name :String): Source{
        return Source(name,name)
    }

}