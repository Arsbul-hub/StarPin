package com.example.starpin

import android.util.JsonReader
import android.util.Log
import com.example.starpin.common.PlayList
import com.google.gson.Gson
import org.json.JSONObject
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import javax.crypto.spec.GCMParameterSpec


class DataManager {
    val gson = Gson()
    fun save(name: String, data: MutableMap<String, PlayList>) {
        val editor = User.pref.edit()
//        val fos = FileOutputStream("t.tmp")
//        val oos = ObjectOutputStream(fos)
//
////        oos.writeInt(12345)
////        oos.writeObject("Today")
//        oos.writeObject(data)
//
//        oos.close()

////        for (list in data.values){
//            for (track in list.tracks){
//
//            }
//        }
        editor.putString(name, gson.toJson(data).toString())
        editor.apply()


        //val file_steam: FileOutputStream = op
    }

    fun get(name: String): MutableMap<String, PlayList> {
        var out =
            mutableMapOf<String, PlayList>() //JSONObject(User.pref.getString("PlayLists", ""))
        val data = User.pref.getString(name, "")
        if (data != "") {
            val json_data = JSONObject(data)

            if (name == "Playlists") {
                for (n in json_data.keys()) {
                    out[n] = gson.fromJson(json_data[n].toString(), PlayList::class.java)

                }
            }
        }

        //Log.e("123", gson.fromJson(User.pref.getString("PlayLists", ""), PlayList::class.java).toString())
        return out
    }
}

class UserManager {

    var play_lists = mutableMapOf<String, PlayList>()
    val data_manager = DataManager()
    fun loadUserData() {
        val geted_playlists = data_manager.get("Playlists")
        if (geted_playlists.size == 0) {
            newPlayList("Понравившиеся")
        } else {
            play_lists = geted_playlists
        }
        //newPlayList("Понравившиеся")
        Log.e("123", "123")
    }

    fun addToPlayList(list_name: String, track: Track) {

        if (play_lists.contains(list_name)) {
            play_lists[list_name]!!.tracks.add(track)

        } else {
            newPlayList(list_name)
            addToPlayList(list_name, track)

        }
        data_manager.save("Playlists", play_lists)

    }

    fun newPlayList(list_name: String) {

        if (!play_lists.contains(list_name)) {

            play_lists[list_name] = PlayList(name = list_name)
        }
    }

    fun deleteFromPlayList(list_name: String, track: Track) {
        if (play_lists[list_name]!!.tracks.contains(track)) {
            play_lists[list_name]!!.tracks.remove(track)
        }

        data_manager.save("Playlists", play_lists)

    }
}