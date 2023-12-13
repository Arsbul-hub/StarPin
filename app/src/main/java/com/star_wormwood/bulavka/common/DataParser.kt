package com.star_wormwood.bulavka.common


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.star_wormwood.bulavka.common.Items.Track
import org.jsoup.Jsoup

val domain = "https://rur.hitmotop.com"
const val DEFAULT_STEP = 48

class ParserStatus() {
    companion object {
        const val OK = 1
        const val EMPTY = 2
        const val CONNECTRION_ERROR = 3
    }
}

fun checkForInternet(context: Context): Boolean {

    // register activity with the connectivity manager service
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // if the android version is equal to M
    // or greater we need to use the
    // NetworkCapabilities to check what type of
    // network has the internet connection

    // Returns a Network object corresponding to
    // the currently active default data network.
    val network = connectivityManager.activeNetwork ?: return false

    // Representation of the capabilities of an active network.
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

    return when {
        // Indicates this network uses a Wi-Fi transport,
        // or WiFi has network connectivity
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

        // Indicates this network uses a Cellular transport. or
        // Cellular has network connectivity
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

        // else return false
        else -> false
    }
}

fun search_music(req: String, start: Int = 0): Triple<List<Track>, Int, Int> {
    var siteData =
        Jsoup.connect("${domain}/search/start/$start?q=" + req.replace(" ", "+")).get().html()

    val out_list = mutableListOf<Track>()
    var max = DEFAULT_STEP
    var step = DEFAULT_STEP
//    NumberFormatException

    if ("<li class=\"pagination__item active\"><b class=\"pagination__link pagination__btn active\">1</b></li>" in siteData) { // если количество треков не превышает 48 штук на странице
        var pagination_list = siteData
        val pagination_numbers = mutableListOf<Int>()
        pagination_list =
            pagination_list.substringAfter("""<li class="pagination__item active"><b class="pagination__link pagination__btn active">1</b></li>""")
        pagination_list = pagination_list.substringBefore("</ul>")
        pagination_list = pagination_list.replace(
            """<li class="pagination__item"><a class="pagination__link pagination__btn" href="/search/start/""",
            ""
        )


        for (i in pagination_list.split("</a></li>")) { // adding number of pages to list

            val s = i.substringBefore("?").replace("\\s".toRegex(), "")
            if (s.isNotEmpty()) {
                pagination_numbers.add(s.toInt())
            }

        }
        max = pagination_numbers.last()
        step = pagination_numbers.first()

    }


    siteData = siteData.substringAfter("""<div class="p-info p-inner">""")
    siteData = siteData.substringAfter("""<ul class="tracks__list">""")
    val li = siteData.split("""<li class="tracks__item track mustoggler"""")



    for (i in li) {
        val track: Track = Track()
        if ("""<div class="track__title">""" in i) {
            var j = i.substringAfter("""<div class="track__title">""")
            j = j.replace("""<div class="track__title">""", "")
            j = j.substringBefore("</div>")
            j = j.replace("\n", "").replace("  ", "").substringAfter(" ")
            track.name = j

            //Log.e("names", i)
        }
        if ("<div class=\"track__desc\">" in i) {
            var j = i.substringAfter("<div class=\"track__desc\">")
                .replace("<div class=\"track__desc\">", "")
            j = j.substringBefore("</div>")
            j = j.replace("\n", "").replace("            ", "").replace("           ", "")
            //Log.e("123", j)
            track.artist = j
        }
        if ("<a data-nopjax href=\"" in i) {
            var j =
                i.substringAfter("<a data-nopjax href=\"")
                    .replace("<a data-nopjax href=\"", "")
            j = j.substringBefore("\" class=\"track__download-btn\"></a>")
            j = j.replace("\n", "").replace("  ", "").substringAfter(" ")
            track.url = j
        }

        if ("""<div class="track__img" style="background-image: url('""" in i) {
            var j =
                i.substringAfter("""<div class="track__img" style="background-image: url('""")
                    .replace(
                        """<div class="track__img" style="background-image: url('""",
                        ""
                    )
            j = j.substringBefore("""');"></div>""")
            j = j.replace("\n", "").replace("  ", "").substringAfter(" ")
            // j = domain + j.replace("https://static.hitmcdn.com", "")
            if ("https://static.hitmcdn.com" in j) {
                j = domain + j.replace("https://static.hitmcdn.com", "")
            }
            track.avatar = j
        }
        if (track.isFull()) {
            //Log.e("rgfdf", dict.toString())
            out_list.add(track)
            //dict.clear()
        }

    }


    return Triple(out_list, step, max)


}


fun get_top(start: Int = 0, maxListLeng: Int? = null): Triple<List<Track>, Int, Int> {


    var siteData = Jsoup.connect("${domain}/songs/top-today/start/$start").get().html()
    val out_top = mutableListOf<Track>()
    var max: Int = DEFAULT_STEP
    var step: Int = DEFAULT_STEP
    Log.e("test", maxListLeng.toString())
    if ("<li class=\"pagination__item active\"><b class=\"pagination__link pagination__btn active\">1</b></li>" in siteData) {

        val pagination_numbers = mutableListOf<Int>()
        var pagination_list = siteData
        pagination_list =
            pagination_list.substringAfter("""<li class="pagination__item active"><b class="pagination__link pagination__btn active">1</b></li>""")
        pagination_list = pagination_list.substringBefore("</ul>")
        pagination_list = pagination_list.replace(
            """<li class="pagination__item"><a class="pagination__link pagination__btn" href="/songs/top-today/start/""",
            ""
        )

        for (i in pagination_list.split("</a></li>")) { // adding number of pages to list
            val s = i.substringBefore("""">""").replace("\\s".toRegex(), "")
            if (s.isNotEmpty()) {
                pagination_numbers.add(s.toInt())
            }

        }

        step = pagination_numbers.first()
        max = pagination_numbers.last()


    }

//    var dt = Jsoup.connect("${domain}/songs/top-today").get().html()

    siteData = siteData.substringAfter("""<div class="p-info p-inner">""")
    siteData = siteData.substringAfter("""<ul class="tracks__list">""")
    val li = siteData.split("""<li class="tracks__item track mustoggler"""")



    for (i in li) {
        val track = Track()
        if ("""<div class="track__title">""" in i) {
            var j = i.substringAfter("""<div class="track__title">""")
            j = j.replace("""<div class="track__title">""", "")
            j = j.substringBefore("</div>")
            j = j.replace("\n", "").replace("  ", "").substringAfter(" ")
            track.name = j

            //Log.e("names", i)
        }
        if ("<div class=\"track__desc\">" in i) {
            var j = i.substringAfter("<div class=\"track__desc\">")
                .replace("<div class=\"track__desc\">", "")
            j = j.substringBefore("</div>")
            j = j.replace("\n", "").replace("            ", "")
                .replace("           ", "")
            //Log.e("123", j)
            track.artist = j
        }
        if ("<a data-nopjax href=\"" in i) {
            var j =
                i.substringAfter("<a data-nopjax href=\"")
                    .replace("<a data-nopjax href=\"", "")
            j = j.substringBefore("\" class=\"track__download-btn\"></a>")
            j = j.replace("\n", "").replace("  ", "").substringAfter(" ")
            track.url = j
        }

        if ("""<div class="track__img" style="background-image: url('""" in i) {
            var j =
                i.substringAfter("""<div class="track__img" style="background-image: url('""")
                    .replace(
                        """<div class="track__img" style="background-image: url('""",
                        ""
                    )
            j = j.substringBefore("""');"></div>""")
            j = j.replace("\n", "").replace("  ", "").substringAfter(" ")
            // j = domain + j.replace("https://static.hitmcdn.com", "")
            if ("https://static.hitmcdn.com" in j) {
                j = domain + j.replace("https://static.hitmcdn.com", "")
            }
            track.avatar = j
        }



        if (track.isFull() && maxListLeng != null && out_top.size < maxListLeng) {

            out_top.add(track)
            //dict.clear()
        } else if (track.isFull() && maxListLeng == null) {

            out_top.add(track)
        }

    }


    return Triple(out_top, step, max)


}




