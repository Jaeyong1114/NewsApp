package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.ActivityMainBinding
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var newsAdapter : NewsAdapter
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.mk.co.kr/")
        .addConverterFactory(
            TikXmlConverterFactory.create(
                TikXml.Builder()
                    .exceptionOnUnreadXml(false)
                    .build()
            )

        ).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newsAdapter = NewsAdapter()
        val newsService = retrofit.create(NewsService::class.java)

        binding.newsRecyclerView.apply{
            layoutManager=LinearLayoutManager(context)
            adapter = newsAdapter
        }

        binding.feedsChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.feedsChip.isChecked = true

            newsService.mainFeed().submitList()
        }
        binding.politicsChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.politicsChip.isChecked = true
            newsService.politicsNews().submitList()

        }
        binding.economyChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.economyChip.isChecked = true

            newsService.economyNews().submitList()

        }

        binding.societyChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.societyChip.isChecked = true

            newsService.societyNews().submitList()

        }
        binding.itChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.itChip.isChecked = true

            newsService.itNews().submitList()
        }
        binding.sportChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.sportChip.isChecked = true

            newsService.sportNews().submitList()

        }



        newsService.mainFeed().submitList()




    }

    private fun Call<NewsRss>.submitList(){
        enqueue(object: Callback<NewsRss>{
            override fun onResponse(call: Call<NewsRss>, response: Response<NewsRss>) {
                Log.e("MainActivity","${response.body()?.channel?.items}")

                val list = response.body()?.channel?.items.orEmpty().transform()
                newsAdapter.submitList(list)

                list.forEachIndexed {index, news ->

                    Thread{
                        try{
                            val jsoup = Jsoup.connect(news.link).get()
                            val elements = jsoup.select("meta[property^=og:]")
                            val ogImageNode = elements.find {node ->
                                node.attr("property") == "og:image"
                            }

                            news.imageUrl=ogImageNode?.attr("content")
                            Log.e("MainActivity","imageUrl :${news.imageUrl}")

                        } catch (e: Exception){
                            e.printStackTrace()
                        }
                        runOnUiThread { newsAdapter.notifyItemChanged(index) }




                    }.start()

                }



            }

            override fun onFailure(call: Call<NewsRss>, t: Throwable) {
                t.printStackTrace()
            }

        })

    }
}