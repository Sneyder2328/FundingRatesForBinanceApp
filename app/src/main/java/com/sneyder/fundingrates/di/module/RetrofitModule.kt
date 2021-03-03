//@file:Suppress("unused")
//
//package com.sneyder.fundingrates.di.module
//
//import com.google.gson.Gson
//import com.google.gson.GsonBuilder
//import com.sneyder.fundingrates.data.api.SpacelensApi
//import dagger.Module
//import dagger.Provides
//import okhttp3.OkHttpClient
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import java.util.concurrent.TimeUnit
//import javax.inject.Singleton
//
//@Module
//class RetrofitModule {
//
//    @Provides
//    @Singleton
//    fun provideHttpClient(): OkHttpClient {
//        return OkHttpClient.Builder()
//            .connectTimeout(45, TimeUnit.SECONDS)
//            .writeTimeout(45, TimeUnit.SECONDS)
//            .readTimeout(45, TimeUnit.SECONDS)
//            .retryOnConnectionFailure(false)
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun gson(): Gson = GsonBuilder()
//        .setLenient()
//        .setLenient()
//        .create()
//
//    @Provides
//    @Singleton
//    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
//        .baseUrl(SpacelensApi.END_POINT)
//        .client(okHttpClient)
//        .addConverterFactory(GsonConverterFactory.create(gson))
//        .build()
//
//    @Provides
//    @Singleton
//    fun provideSpacelensApi(retrofit: Retrofit): SpacelensApi =
//        retrofit.create(SpacelensApi::class.java)
//}