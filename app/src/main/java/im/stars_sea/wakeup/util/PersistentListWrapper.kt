package im.stars_sea.wakeup.util

import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.PersistentList

interface PersistentListWrapper<T> {
    suspend fun size(): Int

    suspend fun add(item: T): Boolean

    suspend fun del(item: T): Boolean

    suspend fun clear()

    val list: PersistentList<T> @Composable get
}