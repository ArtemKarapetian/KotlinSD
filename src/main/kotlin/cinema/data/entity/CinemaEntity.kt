package cinema.data.entity

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalTime

@Serializable
abstract class ObjectWithId(@Transient open var id: Int = 0)

@Serializable
data class Film(
    override var id: Int,
    var title: String,
    @Serializable(with = LocalTimeSerializer::class)
    var startTime: LocalTime,
    @Serializable(with = LocalTimeSerializer::class)
    var endTime: LocalTime,
    ) : ObjectWithId(id) {
    override fun toString(): String {
        return "$id. $title - $startTime - $endTime"
    }
}

object LocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalTime {
        return LocalTime.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: LocalTime) {
        encoder.encodeString(value.toString())
    }
}

@Serializable
data class Ticket(
    override var id: Int,
    val filmId: Int,
    val seats: List<Pair<Int, Int>>,
    ) : ObjectWithId(id) {
    override fun toString(): String {
        return "Билет с id $id. Номер фильма - $filmId, места: $seats"
    }
}
