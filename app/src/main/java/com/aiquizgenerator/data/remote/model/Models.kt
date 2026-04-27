package com.aiquizgenerator.data.remote.model
import com.google.gson.annotations.SerializedName

data class GeminiRequest(val contents: List<Content>, @SerializedName("generationConfig") val generationConfig: GenerationConfig = GenerationConfig())
data class Content(val parts: List<Part>, val role: String = "user")
data class Part(val text: String)
data class GenerationConfig(val temperature: Float = 0.7f, @SerializedName("maxOutputTokens") val maxOutputTokens: Int = 8192)
data class GeminiResponse(val candidates: List<Candidate>?, val error: GeminiError?)
data class Candidate(val content: ContentResponse?, @SerializedName("finishReason") val finishReason: String?)
data class ContentResponse(val parts: List<PartResponse>?, val role: String?)
data class PartResponse(val text: String?)
data class GeminiError(val code: Int, val message: String, val status: String)

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    @SerializedName("correct_answer") val correctAnswer: String,
    var userAnswer: String? = null,
    var isMarkedForReview: Boolean = false,
    var isAnswered: Boolean = false
)

data class QuizConfig(
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val numberOfQuestions: Int = 10,
    val timerDurationMinutes: Int = 15,
    val extractedText: String = "",
    val fileName: String = ""
)

enum class Difficulty(val label: String) { EASY("Easy"), MEDIUM("Medium"), HARD("Hard") }

data class QuizResult(
    val totalQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val unattempted: Int,
    val score: Int,
    val questions: List<QuizQuestion>
)
