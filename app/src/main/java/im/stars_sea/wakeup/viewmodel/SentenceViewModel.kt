package im.stars_sea.wakeup.viewmodel

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.MultiProcessDataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.lifecycle.AndroidViewModel
import im.stars_sea.wakeup.data.Sentence
import im.stars_sea.wakeup.data.SentenceConfigs
import im.stars_sea.wakeup.data.SentenceType
import im.stars_sea.wakeup.network.randomSentence
import im.stars_sea.wakeup.serializer.SentenceConfigsSerializer
import kotlinx.collections.immutable.mutate
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

class SentenceViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = MultiProcessDataStoreFactory.create(
        serializer = SentenceConfigsSerializer,
        produceFile = { application.dataStoreFile("sentence.json") }
    )

    private val configs @Composable get() = dataStore.data.collectAsState(initial = SentenceConfigs()).value

    val currentSentence @Composable get() = configs.current
    val collectedSentences @Composable get() = configs.collected
    val bannedSentences @Composable get() = configs.banned

    private suspend fun getCurrentSentence() = dataStore.data.first().current

    suspend fun updateCurrentSentence(sentence: Sentence): Sentence {
        dataStore.updateData { configs ->
            configs.copy(current = sentence)
        }
        return sentence
    }

    suspend fun updateCurrentSentence(type: SentenceType = SentenceType.Poem): Sentence {
        var sentence = randomSentence(type)
        var counter = 0
        while (sentence.isBanned() && counter < 4) {
            delay(2000)
            sentence = randomSentence(type)
            counter++
        }

        return updateCurrentSentence(sentence)
    }

    suspend fun getOrCreateSentence(type: SentenceType = SentenceType.Poem): Sentence {
        val current = getCurrentSentence()
        if (current != Sentence.Empty) return current
        return updateCurrentSentence(type)
    }


    suspend fun collectSentence(sentence: Sentence): Boolean {
        if (sentence == Sentence.Empty) return false

        var result = false
        dataStore.updateData { configs ->
            configs.copy(
                collected = configs.collected.mutate { result = it.add(sentence) }
            )
        }
        return result
    }

    suspend fun cancelCollectSentence(sentence: Sentence): Boolean { // Chinglish _(:з)∠)_
        var result = false
        dataStore.updateData { configs ->
            configs.copy(
                collected = configs.collected.mutate { result = it.remove(sentence) }
            )
        }
        return result
    }

    suspend fun collectCurrent() = collectSentence(getCurrentSentence())
    suspend fun cancelCollectCurrent() = cancelCollectSentence(getCurrentSentence())


    suspend fun banSentence(sentence: Sentence): Boolean {
        if (sentence == Sentence.Empty) return false

        var result = false
        dataStore.updateData { configs ->
            configs.copy(
                banned = configs.banned.mutate { result = it.add(sentence) }
            )
        }
        return result
    }

    suspend fun unbanSentence(sentence: Sentence): Boolean {
        var result = false
        dataStore.updateData { configs ->
            configs.copy(
                banned = configs.banned.mutate { result = it.remove(sentence) }
            )
        }
        return result
    }

    suspend fun banCurrentSentence() = banSentence(getCurrentSentence())
    suspend fun unbanCurrentSentence() = unbanSentence(getCurrentSentence())


    suspend fun Sentence.isCollected() = dataStore.data.first().collected.contains(this)
    suspend fun Sentence.isBanned() = dataStore.data.first().banned.contains(this)
}