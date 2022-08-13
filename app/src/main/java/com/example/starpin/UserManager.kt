package com.example.starpin


data class PlayList(var tracks: MutableList<Track> = mutableListOf())
var play_lists = mutableMapOf<String, PlayList>()
class UserManager {


    init {
        addNewPlayList("Понравившиеся")

    }

    fun addToPlayList(list_name: String, track: Track) {
        play_lists[list_name]!!.tracks.add(track)
    }

    fun addNewPlayList(list_name: String) {
        play_lists[list_name] = PlayList()
    }
}