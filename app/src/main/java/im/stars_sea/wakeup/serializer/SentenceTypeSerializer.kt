package im.stars_sea.wakeup.serializer

import im.stars_sea.wakeup.data.SentenceType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class SentenceTypeSerializer : KSerializer<SentenceType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("SentenceType", PrimitiveKind.CHAR)

    override fun deserialize(decoder: Decoder): SentenceType {
        val code = decoder.decodeChar()
        return SentenceType.entries.toTypedArray().first { code == it.code }
    }

    override fun serialize(encoder: Encoder, value: SentenceType) {
        encoder.encodeChar(value.code)
    }
}