package im.stars_sea.wakeup

import android.app.AlarmManager
import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import im.stars_sea.wakeup.service.WakeUpService
import im.stars_sea.wakeup.util.NotificationHelper
import im.stars_sea.wakeup.service.WakeUpServiceConnection

class WakeUpApp : Application() {
    lateinit var alarmManager: AlarmManager
        private set
    lateinit var notificationManager: NotificationManager
        private set

    lateinit var sentenceNotification: NotificationHelper
        private set
    lateinit var alarmNotification: NotificationHelper
        private set

    private fun registerNotificationChannels() {
        sentenceNotification = NotificationHelper(
            id = 0xDF,
            name = getString(R.string.sentence_daily_notification),
            wakeUpApp = this,
            importance = NotificationManager.IMPORTANCE_LOW
        ) {
            it.lockscreenVisibility = Notification.VISIBILITY_SECRET
        }
        alarmNotification = NotificationHelper(
            id = 0x96,
            name = getString(R.string.alarm_notification),
            wakeUpApp = this,
            importance = NotificationManager.IMPORTANCE_HIGH
        ) {
            it.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            it.setBypassDnd(true)
            it.setAllowBubbles(true)
            it.enableLights(true)
            it.enableVibration(true)
        }
    }

    override fun onCreate() {
        super.onCreate()

        Log.i("WakeUpApp", "init")

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        registerNotificationChannels()

        if (getProcessName().equals("$packageName:remote")) return
        val startServiceIntent = Intent(this, WakeUpService::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startForegroundService(startServiceIntent)
        bindService(
            startServiceIntent,
            WakeUpServiceConnection,
            BIND_AUTO_CREATE
        )
    }
}
