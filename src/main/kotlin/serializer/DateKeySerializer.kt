package serializer

import data.DateKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object DateKeySerializer: KSerializer<DateKey> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("DateKey", PrimitiveKind.INT)
    override fun serialize(encoder: Encoder, value: DateKey) = encoder.encodeInt(value.toInt())
    override fun deserialize(decoder: Decoder): DateKey = DateKey(decoder.decodeInt())
}
