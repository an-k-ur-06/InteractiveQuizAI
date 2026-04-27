package com.aiquizgenerator.data.local.dao
import androidx.lifecycle.LiveData
import androidx.room.*
import com.aiquizgenerator.data.local.entity.QuizQuestionEntity
import com.aiquizgenerator.data.local.entity.QuizSessionEntity

@Dao interface QuizSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(s: QuizSessionEntity)
    @Update suspend fun update(s: QuizSessionEntity)
    @Query("SELECT * FROM quiz_sessions ORDER BY completedAt DESC") fun getAll(): LiveData<List<QuizSessionEntity>>
}

@Dao interface QuizQuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(q: List<QuizQuestionEntity>)
    @Query("SELECT * FROM quiz_questions WHERE sessionId=:id ORDER BY questionIndex ASC") suspend fun getForSession(id: String): List<QuizQuestionEntity>
}
