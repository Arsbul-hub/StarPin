package com.star_wormwood.bulavka.common.BroadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.star_wormwood.bulavka.Managers
import com.star_wormwood.bulavka.common.Managers.MusicManager

class HeadphonesBroadcastReceiver : BroadcastReceiver() {
    var oldPlayingState = 0
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent!!.action



    }

}