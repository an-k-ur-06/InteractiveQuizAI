package com.aiquizgenerator.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.aiquizgenerator.data.local.Converters

@Entity(tableName = "quiz_sessions")
data class QuizSessionEntity(
    @PrimaryKey val sessionId: String,
    val fileName: String,
    val difficulty: String,
    val totalQuestions: Int,
    val timerDuration: Int,
    val score: Int = 0,
    val correctAnswers: Int = 0,
    val wrongAnswers: Int = 0,
    val unattempted: Int = 0,
    val completedAt: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false
)

@Entity(tableName = "quiz_questions")
@TypeConverters(Converters::class)
data class QuizQuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val userAnswer: String? = null,
    val questionIndex: Int
)
