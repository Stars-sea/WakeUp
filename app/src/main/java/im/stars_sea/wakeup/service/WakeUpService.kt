package im.stars_sea.wakeup.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import im.stars_sea.wakeup.WakeUpApp
import im.stars_sea.wakeup.data.AlarmTime
import im.stars_sea.wakeup.ui.AlarmActivity
import im.stars_sea.wakeup.util.NotificationHelper

class WakeUpService : Service() {
    private val alarmManager: AlarmManager get() = (application as WakeUpApp).alarmManager
    private val alarmNotification: NotificationHelper get() = (application as WakeUpApp).alarmNotification

    internal val sentenceNotification: NotificationHelper get() = (application as WakeUpApp).sentenceNotification

    private var alarmRequestPendingIntent: PendingIntent? = null

    private fun notifyAlarm(alarm: AlarmTime) {
        alarmNotification.notify(
            applicationContext,
            "嘿, 醒醒",
            "现在是 $alarm",
            true
        )
    }

    internal fun setAlarm(alarm: AlarmTime) {
        val intent = Intent(this, AlarmActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        alarmRequestPendingIntent = PendingIntent.getActivity(
            applicationContext,
            alarm.minutesOfDay,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarm.toCalendar().timeInMillis,
            alarmRequestPendingIntent!!
        )
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarm.toCalendar().timeInMillis,
            "Task$alarm",
            { notifyAlarm(alarm) },
            null
        )
    }

    internal fun clearAlarms() {
        if (alarmRequestPendingIntent == null) return
        alarmManager.cancel(alarmRequestPendingIntent!!)
        alarmRequestPendingIntent = null
    }

    override fun onBind(intent: Intent): IBinder = WakeUpServiceBinder(this)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val notification = sentenceNotification.notify(applicationContext,  content = "加载中~")
        startForeground(sentenceNotification.id, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED)
        return START_STICKY
    }

    override fun onDestroy() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }
}
