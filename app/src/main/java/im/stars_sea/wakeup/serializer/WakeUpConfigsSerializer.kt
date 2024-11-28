package im.stars_sea.wakeup.serializer

import androidx.datastore.core.Serializer
import im.stars_sea.wakeup.data.WakeUpConfigs
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object WakeUpConfigsSerializer : Serializer<WakeUpConfigs> {
    override val defaultValue: WakeUpConfigs
        get() = WakeUpConfigs()

    override suspend fun readFrom(input: InputStream): WakeUpConfigs {
        return try {
            Json.decodeFromString(
                deserializer = WakeUpConfigs.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: WakeUpConfigs, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = WakeUpConfigs.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}
