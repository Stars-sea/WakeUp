package im.stars_sea.wakeup.service

import android.os.Parcel
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.MultiProcessDataStoreFactory
import androidx.datastore.dataStoreFile
import im.stars_sea.wakeup.data.AlarmTime
import im.stars_sea.wakeup.data.Sentence
import im.stars_sea.wakeup.data.SentenceConfigs
import im.stars_sea.wakeup.data.SentenceType
import im.stars_sea.wakeup.data.WakeUpConfigs
import im.stars_sea.wakeup.network.randomSentence
import im.stars_sea.wakeup.serializer.SentenceConfigsSerializer
import im.stars_sea.wakeup.serializer.WakeUpConfigsSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private const val TAG = "WakeUpService"

class WakeUpServiceBinder(private val service: WakeUpService) : IWakeUpService.Stub() {
    private val dataStore: DataStore<WakeUpConfigs> = MultiProcessDataStoreFactory.create(
        serializer = WakeUpConfigsSerializer,
        produceFile = { service.dataStoreFile("configs.json") }
    )
    private val sentenceDataStore: DataStore<SentenceConfigs> = MultiProcessDataStoreFactory.create(
        serializer = SentenceConfigsSerializer,
        produceFile = { service.dataStoreFile("sentence.json") }
    )

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
        runBlocking {
            sentenceDataStore.updateData { configs ->
                configs.copy(current = randomSentence(SentenceType.Poem))
            }
        }

        refreshAlarms()
        refreshSentence()
    }

    override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
        try {
            return super.onTransact(code, data, reply, flags)
        } catch (e: Exception) {
            Log.e(TAG, "Exception caught", e)
            throw e
        }
    }
}