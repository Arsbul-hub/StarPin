package com.star_wormwood.bulavka.common.Services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat

class Test : Service() {
    companion object {
        fun startService(context: Context) {
            val startIntent = Intent(context, Test::class.java)
            ContextCompat.startForegroundService(context, startIntent)
        }
        fun stopService(context: Context) {
            val stopIntent = Intent(context, Test::class.java)
            context.stopService(stopIntent)
        }
    }
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("action", intent!!.action.toString())
        Thread {        while (true) {
            Log.e("TestService", "working")
        }}.start()
        return super.onStartCommand(intent, flags, startId)
    }
}