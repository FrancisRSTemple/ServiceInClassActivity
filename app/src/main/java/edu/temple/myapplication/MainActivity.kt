package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button

class MainActivity : AppCompatActivity() {

    lateinit var timerBinder : TimerService.TimerBinder
    var isConnected = false
    private lateinit var serviceConnection: ServiceConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serviceConnection = object : ServiceConnection{
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                timerBinder = p1 as TimerService.TimerBinder
                isConnected = true
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                isConnected = false
            }
        }

        bindService(Intent(this, TimerService::class.java), serviceConnection, BIND_AUTO_CREATE)

        findViewById<Button>(R.id.startButton).setOnClickListener {
            if (isConnected) timerBinder.start(10)
        }

        findViewById<Button>(R.id.pauseButton).setOnClickListener {
            if (isConnected) timerBinder.pause()
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if (isConnected) timerBinder.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}