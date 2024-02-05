package com.rbrauwers.newsapp.foregroundservice

import android.app.ForegroundServiceStartNotAllowedException
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.rbrauwers.newsapp.R
import com.rbrauwers.newsapp.SERVICE_CHANNEL_ID

internal class NewsService : Service() {

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.Start.name -> startForeground()
            Actions.Stop.name -> stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForeground() {
        try {
            val notification = NotificationCompat.Builder(this, SERVICE_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Syncing...")
                .setContentText("In progress")
                .build()

            ServiceCompat.startForeground(
                this,
                SERVICE_ID, // Cannot be 0
                notification,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                } else {
                   0
               }
            )
        } catch (e: Exception) {
            println(e)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && e is ForegroundServiceStartNotAllowedException
            ) {
                // App not in a valid state to start foreground service
                // (e.g. started from bg)
                println("Could not start NewsService: app is an invalid state")
            }
        }
    }

    enum class Actions {
        Start, Stop
    }

    companion object {
        const val SERVICE_ID = 100

        fun run(action: Actions, context: Context) {
            val intent = Intent(context, NewsService::class.java).apply {
                this.action = action.name
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

    }

}