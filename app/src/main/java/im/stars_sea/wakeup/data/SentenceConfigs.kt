package im.stars_sea.wakeup.data

import im.stars_sea.wakeup.serializer.PersistentListSerializer
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class SentenceConfigs(
    @Serializable(with = PersistentListSerializer::class)
    val collected: PersistentList<Sentence> = persistentListOf(),
    @Serializable(with = PersistentListSerializer::class)
    val banned: PersistentList<Sentence> = persistentListOf(),

    val current: Sentence = Sentence.Empty
);