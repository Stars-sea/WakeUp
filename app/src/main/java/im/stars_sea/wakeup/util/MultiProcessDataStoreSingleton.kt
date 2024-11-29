package im.stars_sea.wakeup.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.MultiProcessDataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import androidx.lifecycle.AndroidViewModel

private object MultiProcessDataStoreSingleton {
    private val dataStores: MutableMap<String, DataStore<*>> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T> multiProcessDataStore(
        context: Context,
        fileName: String,
        serializer: Serializer<T>
    ): DataStore<T> {
        if (dataStores.containsKey(fileName))
            return dataStores[fileName]!! as DataStore<T>

        val dataStore = MultiProcessDataStoreFactory.create(
            serializer = serializer,
            produceFile = { context.dataStoreFile(fileName) }
        )
        dataStores[fileName] = dataStore
        return dataStore
    }
}

fun <T> AndroidViewModel.multiProcessDataStore(fileName: String, serializer: Serializer<T>): Lazy<DataStore<T>>
    = lazyOf(MultiProcessDataStoreSingleton.multiProcessDataStore(getApplication(), fileName, serializer))

fun <T> Context.multiProcessDataStore(fileName: String, serializer: Serializer<T>): Lazy<DataStore<T>>
    = lazyOf(MultiProcessDataStoreSingleton.multiProcessDataStore(this, fileName, serializer))
