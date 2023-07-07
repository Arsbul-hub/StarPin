package com.star_wormwood.bulavka.common.BroadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.star_wormwood.bulavka.Managers

private const val TAG = "MyBroadcastReceiver"

class PlayBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action.toString()

        Log.e("state", action)
        if (action == "pause") {
            Managers.musicManager.pause()
        } else if (action == "unpause") {
            Managers.musicManager.unpause()
        } else if (action == "next") {
            Managers.musicManager.nextTrack()
        } else if (action == "previous") {
            Managers.musicManager.previousTrack()
        }


    }
}