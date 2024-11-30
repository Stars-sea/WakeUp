package im.stars_sea.wakeup.viewmodel

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.AndroidViewModel
import im.stars_sea.wakeup.data.Sentence
import im.stars_sea.wakeup.data.SentenceConfigs
import im.stars_sea.wakeup.data.SentenceType
import im.stars_sea.wakeup.network.randomSentence
import im.stars_sea.wakeup.serializer.SentenceConfigsSerializer
import im.stars_sea.wakeup.util.PersistentListWrapper
import im.stars_sea.wakeup.util.multiProcessDataStore
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

class SentenceViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore by multiProcessDataStore("sentence.json", SentenceConfigsSerializer)

    private val configs @Composable get() = dataStore.data.collectAsState(initial = SentenceConfigs()).value

    val currentSentence @Composable get() = configs.current

    val collected = object : PersistentListWrapper<Sentence> {
        override suspend fun size(): Int = dataStore.data.first().collected.size

        override suspend fun clear() {
            dataStore.updateData { configs ->
                configs.copy(
                    collected = persistentListOf()
                )
            }
        }

        override val list @Composable get() = configs.collected

        override suspend fun del(item: Sentence): Boolean {
            var result = false
            dataStore.updateData { configs ->
                configs.copy(
                    collected = configs.collected.mutate { result = it.remove(item) }
                )
            }
            return result
        }

        override suspend fun add(item: Sentence): Boolean {
            if (item == Sentence.Empty) return false

            var result = false
            dataStore.updateData { configs ->
                configs.copy(
                    collected = configs.collected.mutate { result = it.add(item) }
                )
            }
            return result
        }
    }

    val banned = object : PersistentListWrapper<Sentence> {
        override suspend fun size(): Int = dataStore.data.first().banned.size

        override suspend fun clear() {
            dataStore.updateData { configs ->
                configs.copy(
                    banned = persistentListOf()
                )
            }
        }

        override val list @Composable get() = configs.banned

        override suspend fun del(item: Sentence): Boolean {
            var result = false
            dataStore.updateData { configs ->
                configs.copy(
                    banned = configs.banned.mutate { result = it.remove(item) }
                )
            }
            return result
        }

        override suspend fun add(item: Sentence): Boolean {
            if (item == Sentence.Empty) return false

            var result = false
            dataStore.updateData { configs ->
                configs.copy(
                    banned = configs.banned.mutate { result = it.add(item) }
                )
            }
            return result
        }
    }

    private suspend fun getCurrentSentence() = dataStore.data.first().current

    suspend fun updateCurrentSentence(sentence: Sentence): Sentence {
        dataStore.updateData { configs ->
            configs.copy(current = sentence)
        }
        return sentence
    }

    suspend fun updateCurrentSentence(type: SentenceType = SentenceType.Poem): Sentence {
        var counter = 0
        var sentence: Sentence?
        do {
            sentence = try {
                randomSentence(type)
            } catch(e: Throwable) {
                if (counter >= 4) throw e
                null
            }
            if (sentence != null) continue

            delay(2000)
        } while ((sentence == null || isBanned(sentence)) && counter++ < 4)

        return if (sentence != null)
            updateCurrentSentence(sentence)
        else Sentence.Empty
    }

    suspend fun getOrCreateSentence(type: SentenceType = SentenceType.Poem): Sentence {
        val current = getCurrentSentence()
        if (current != Sentence.Empty) return current
        return updateCurrentSentence(type)
    }

    suspend fun collectCurrent() = collected.add(getCurrentSentence())
    suspend fun cancelCollectCurrent() = collected.del(getCurrentSentence())

    suspend fun banCurrentSentence() = banned.add(getCurrentSentence())
    suspend fun unbanCurrentSentence() = banned.del(getCurrentSentence())

    suspend fun isCollected(sentence: Sentence) = dataStore.data.first().collected.contains(sentence)
    suspend fun isBanned(sentence: Sentence) = dataStore.data.first().collected.contains(sentence)
}