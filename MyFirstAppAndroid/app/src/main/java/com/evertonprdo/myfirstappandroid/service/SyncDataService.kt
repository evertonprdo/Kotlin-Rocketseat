package com.evertonprdo.myfirstappandroid.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class SyncDataService : Service() {
    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        Thread {
            for (i in 1..100) {
                Log.d("SyncDataService", "Sync in progress ($i%)")
                Thread.sleep(500)
            }
            stopSelf()
        }.start()

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}