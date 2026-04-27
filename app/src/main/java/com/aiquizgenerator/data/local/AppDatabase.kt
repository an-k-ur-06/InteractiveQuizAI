package com.aiquizgenerator.data.local
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aiquizgenerator.data.local.dao.QuizQuestionDao
import com.aiquizgenerator.data.local.dao.QuizSessionDao
import com.aiquizgenerator.data.local.entity.QuizQuestionEntity
import com.aiquizgenerator.data.local.entity.QuizSessionEntity

@Database(entities = [QuizSessionEntity::class, QuizQuestionEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): QuizSessionDao
    abstract fun questionDao(): QuizQuestionDao
}
