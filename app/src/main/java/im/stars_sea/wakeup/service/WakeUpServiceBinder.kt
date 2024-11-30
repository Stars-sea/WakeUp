package im.stars_sea.wakeup.service

import android.os.Parcel
import android.util.Log
import im.stars_sea.wakeup.data.AlarmTime
import im.stars_sea.wakeup.data.Sentence
import im.stars_sea.wakeup.data.SentenceType
import im.stars_sea.wakeup.network.randomSentence
import im.stars_sea.wakeup.serializer.SentenceConfigsSerializer
import im.stars_sea.wakeup.serializer.WakeUpConfigsSerializer
import im.stars_sea.wakeup.util.multiProcessDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private const val TAG = "WakeUpService"

class WakeUpServiceBinder(private val service: WakeUpService) : IWakeUpService.Stub() {
    private val dataStore by service.multiProcessDataStore("configs.json", WakeUpConfigsSerializer)
    private val sentenceDataStore by service.multiProcessDataStore("sentence.json", SentenceConfigsSerializer)

    override fun notifyString(text: String?) {
        service.sentenceNotification.notify(service, content = text ?: "喂, 快醒醒")
    }

    override fun notifySentence(sentence: Sentence?) {
        service.sentenceNotification.notify(service, sentence!!)
    }

    override fun refreshSentence() = runBlocking(Dispatchers.IO) {
        val current = sentenceDataStore.data.first().current
        if (current != Sentence.Empty) notifySentence(current)
    }

    override fun refreshAlarms(): Unit = runBlocking(Dispatchers.IO) {
        service.clearAlarms()

        val current = AlarmTime()
        val nearestTime = dataStore.data.first().alarms
            .filter { it.enabled && it.time != current }
            .map { it.time }
            .minByOrNull { it - current }

        if (nearestTime == null) return@runBlocking
        service.setAlarm(nearestTime)

        Log.i(TAG, "refreshed, new time: $nearestTime")
    }

    override fun init() {
        runBlocking(Dispatchers.IO) {
            try {
                sentenceDataStore.updateData { configs ->
                    configs.copy(current = randomSentence(SentenceType.Poem))
                }
            } catch (e: Exception) {
                Log.w(TAG, e)
                e.printStackTrace()
            }
        }

        refreshAlarms()
        refreshSentence()
    }

    override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
        try {
            return super.onTransact(code, data, reply, flags)
        } catch (e: Throwable) {
            Log.e(TAG, "Exception caught", e)
            throw e
        }
    }
}