package com.example.starpin

import android.provider.DocumentsContract
import android.util.Log
import org.jsoup.Jsoup

class parser() {
    fun get_top() {
        Thread{
            var dt = Jsoup.connect("https://ru.hitmotop.com/songs/top-today").get().html()
            dt = dt.substringAfter("""<div class="p-info p-inner">""")
            dt = dt.substringAfter("""<ul class="tracks__list">""")
            var li = dt.split("""<li class="tracks__item track mustoggler"""")
            for (i in li) {
                if ("""<div class="track__title">""" in i){
                    var i = i.substringAfter("""<div class="track__title">""")
                    i = i.replace("""<div class="track__title">""", "")
                    i = i.substringBefore("</div>")
                    i = i.replace("\n", "").replace("  ", "").substringAfter(" ")


                    //Log.e("names", i)
                    }
                if ("<a data-nopjax href=\"" in i) {
                    Log.e("Test", "Yes")

                }
            }
        }.start()
    }
}