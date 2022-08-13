package com.example.starpin

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.music_item.view.*
import org.jsoup.Jsoup

// r.get("https://ru.hitmotop.com/search?q=" + req.replace(" ", "+")).text
fun search_music(req: String): List<Track> {

    var dt = Jsoup.connect("https://ru.hitmotop.com/search?q=" + req.replace(" ", "+")).get().html()
    dt = dt.substringAfter("""<div class="p-info p-inner">""")
    dt = dt.substringAfter("""<ul class="tracks__list">""")
    val li = dt.split("""<li class="tracks__item track mustoggler"""")


    val out_list = mutableListOf<MutableMap<String, String>>()
    for (i in li) {
        val dict: MutableMap<String, String> = mutableMapOf()
        if ("""<div class="track__title">""" in i) {
            var j = i.substringAfter("""<div class="track__title">""")
            j = j.replace("""<div class="track__title">""", "")
            j = j.substringBefore("</div>")
            j = j.replace("\n", "").replace("  ", "").substringAfter(" ")
            dict.put("name", j)

            //Log.e("names", i)
        }
        if ("<div class=\"track__desc\">" in i) {
            var j = i.substringAfter("<div class=\"track__desc\">")
                .replace("<div class=\"track__desc\">", "")
            j = j.substringBefore("</div>")
            j = j.replace("\n", "").replace("            ", "").replace("           ", "")
            //Log.e("123", j)
            dict.put("artist", j)
        }
        if ("<a data-nopjax href=\"" in i) {
            var j = i.substringAfter("<a data-nopjax href=\"").replace("<a data-nopjax href=\"", "")
            j = j.substringBefore("\" class=\"track__download-btn\"></a>")
            j = j.replace("\n", "").replace("  ", "").substringAfter(" ")
            dict.put("url", j)
        }

        if ("""<div class="track__img" style="background-image: url('""" in i) {
            var j = i.substringAfter("""<div class="track__img" style="background-image: url('""")
                .replace("""<div class="track__img" style="background-image: url('""", "")
            j = j.substringBefore("""');"></div>""")
            j = j.replace("\n", "").replace("  ", "").substringAfter(" ")
            j = "https://ru.hitmotop.com" + j
            dict.put("avatar", j)
        }
        if (dict.size == 4) {
            //Log.e("rgfdf", dict.toString())
            out_list.add(dict)
            //dict.clear()
        }
    }
    return out_list.map{
        Track(name=it["name"]!!, artist=it["artist"]!!, url=it["url"]!!, avatar=it["avatar"]!!)

    }
}

fun get_top(): List<Track> {


    var dt = Jsoup.connect("https://ru.hitmotop.com/songs/top-today").get().html()

    dt = dt.substringAfter("""<div class="p-info p-inner">""")
    dt = dt.substringAfter("""<ul class="tracks__list">""")
    val li = dt.split("""<li class="tracks__item track mustoggler"""")


    val out_top = mutableListOf<MutableMap<String, String>>()
    for (i in li) {
        val dict: MutableMap<String, String> = mutableMapOf()
        if ("""<div class="track__title">""" in i) {
            var j = i.substringAfter("""<div class="track__title">""")
            j = j.replace("""<div class="track__title">""", "")
            j = j.substringBefore("</div>")
            j = j.replace("\n", "").replace("  ", "").substringAfter(" ")
            dict.put("name", j)

            //Log.e("names", i)
        }
        if ("<div class=\"track__desc\">" in i) {
            var j = i.substringAfter("<div class=\"track__desc\">")
                .replace("<div class=\"track__desc\">", "")
            j = j.substringBefore("</div>")
            j = j.replace("\n", "").replace("            ", "").replace("           ", "")
            //Log.e("123", j)
            dict.put("artist", j)
        }
        if ("<a data-nopjax href=\"" in i) {
            var j = i.substringAfter("<a data-nopjax href=\"").replace("<a data-nopjax href=\"", "")
            j = j.substringBefore("\" class=\"track__download-btn\"></a>")
            j = j.replace("\n", "").replace("  ", "").substringAfter(" ")
            dict.put("url", j)
        }

        if ("""<div class="track__img" style="background-image: url('""" in i) {
            var j = i.substringAfter("""<div class="track__img" style="background-image: url('""")
                .replace("""<div class="track__img" style="background-image: url('""", "")
            j = j.substringBefore("""');"></div>""")
            j = j.replace("\n", "").replace("  ", "").substringAfter(" ")
            j = "https://ru.hitmotop.com" + j
            dict.put("avatar", j)
        }
        if (dict.size == 4) {
            //Log.e("rgfdf", dict.toString())
            out_top.add(dict)
            //dict.clear()
        }
    }
    return out_top.map{
        Track(name=it["name"]!!, artist=it["artist"]!!, url=it["url"]!!, avatar=it["avatar"]!!)

    }
}




