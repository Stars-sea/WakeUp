package im.stars_sea.wakeup.serializer

import androidx.datastore.core.Serializer
import im.stars_sea.wakeup.data.SentenceConfigs
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object SentenceConfigsSerializer : Serializer<SentenceConfigs> {
    override val defaultValue: SentenceConfigs
        get() = SentenceConfigs()

    override suspend fun readFrom(input: InputStream): SentenceConfigs {
        return try {
            Json.decodeFromString(
                deserializer = SentenceConfigs.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: SentenceConfigs, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = SentenceConfigs.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}