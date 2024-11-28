package im.stars_sea.wakeup.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import im.stars_sea.wakeup.R
import im.stars_sea.wakeup.WakeUpApp
import im.stars_sea.wakeup.data.Sentence
import im.stars_sea.wakeup.ui.MainActivity
import im.stars_sea.wakeup.ui.component.WakeUpTabItem

class NotificationHelper(
    val id: Int,
    val name: String,
    wakeUpApp: WakeUpApp,
    val importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
    operations: (NotificationChannel) -> Unit = { }
) {
    private val manager = wakeUpApp.notificationManager
    private val channelId = "$name$id"

    init {
        val channel = NotificationChannel(channelId, name, importance)
        operations(channel)
        manager.createNotificationChannel(channel)
    }

    fun notify(
        context: Context,
        title: String = context.getString(R.string.app_name),
        content: String,
        ongoing: Boolean = false,
        operations: (NotificationCompat.Builder) -> Unit = { }
    ): Notification {
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_wakeup)
            .setContentTitle(title)
            .setContentText(content)
            .setOngoing(ongoing)
        operations(notificationBuilder)

        val notification = notificationBuilder.build()

        manager.notify(id, notification)
        return notification
    }

    fun notify(
        context: Context,
        sentence: Sentence,
        operations: (NotificationCompat.Builder) -> Unit = { }
    ): Notification {
        val viewSentence = Intent(context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            .putExtra("tab", WakeUpTabItem.Sentences)
        val pendingIntent = PendingIntent.getActivity(
            context,
            sentence.id,
            viewSentence,
            PendingIntent.FLAG_IMMUTABLE
        )

        return notify(context, sentence.hitokoto, sentence.author) {
            it.setContentIntent(pendingIntent)
            operations(it)
        }
    }
}