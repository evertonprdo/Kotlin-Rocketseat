package com.evertonprdo.myfirstappandroid.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class LowBatteryBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Low battery event received", Toast.LENGTH_LONG)
            .show()
    }
}