package com.evertonprdo.myfirstappandroid

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.evertonprdo.myfirstappandroid.broadcastreceiver.LowBatteryBroadcastReceiver
import com.evertonprdo.myfirstappandroid.databinding.ActivityMainBinding
import com.evertonprdo.myfirstappandroid.service.SyncDataService

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val lowBatteryBroadcastReceiver = LowBatteryBroadcastReceiver()
    private val lowBatteryIntentFilter =
        IntentFilter("android.intent.action.BATTERY_LOW")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        showToast(context = this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        with(binding.tvMyFirstAndroidApp) {
            this?.text = "Hello World!!!"
            this?.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        }

        val color = getColor(R.color.white)

        supportFragmentManager.beginTransaction().add(
            R.id.flMainContainer,
            BlankFragment.newInstance(
                name = "John doe",
                age = 29,
                isMale = true
            )
        ).commit()

        registerReceiver(lowBatteryBroadcastReceiver, lowBatteryIntentFilter)

        val intent = Intent(this, SyncDataService::class.java)
        startService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(lowBatteryBroadcastReceiver)
    }

    fun showToast(context: Context) {
        Toast.makeText(context, "Hello World!", Toast.LENGTH_SHORT).show()
    }
}