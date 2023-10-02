package com.example.newsapp

import retrofit2.Call
import retrofit2.http.GET

interface NewsService {
    @GET("rss/30000001/")
    fun mainFeed() : Call<NewsRss>

    @GET("rss/30200030/")
    fun politicsNews(): Call<NewsRss>

    @GET("rss/50700001/")
    fun itNews(): Call<NewsRss>

    @GET("rss/71000001/")
    fun sportNews(): Call<NewsRss>

    @GET("rss/50400012/")
    fun societyNews(): Call<NewsRss>

    @GET("rss/30100041/")
    fun economyNews(): Call<NewsRss>



}