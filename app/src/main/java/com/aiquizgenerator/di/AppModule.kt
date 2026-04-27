package com.aiquizgenerator.di
import android.content.Context
import androidx.room.Room
import com.aiquizgenerator.BuildConfig
import com.aiquizgenerator.data.local.AppDatabase
import com.aiquizgenerator.data.local.dao.QuizQuestionDao
import com.aiquizgenerator.data.local.dao.QuizSessionDao
import com.aiquizgenerator.data.remote.api.GeminiApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton fun provideOkHttp(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build()

    @Provides @Singleton fun provideRetrofit(ok: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl("https://generativelanguage.googleapis.com/")
            .client(ok).addConverterFactory(GsonConverterFactory.create()).build()

    @Provides @Singleton fun provideGemini(r: Retrofit): GeminiApiService = r.create(GeminiApiService::class.java)

    @Provides @Singleton fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "quiz_db").fallbackToDestructiveMigration().build()

    @Provides fun provideSessionDao(db: AppDatabase): QuizSessionDao = db.sessionDao()
    @Provides fun provideQuestionDao(db: AppDatabase): QuizQuestionDao = db.questionDao()
    @Provides @Singleton fun provideApiKey(): String = BuildConfig.GEMINI_API_KEY
}
