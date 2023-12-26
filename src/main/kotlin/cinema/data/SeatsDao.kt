package cinema.data

import cinema.data.interfaces.SeatsDaoInterface
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class SeatsDao : SeatsDaoInterface {
    override var filePath: String = "src/main/resources/seats.json"

    private var _cinemaSize = Pair(9, 9)
    override var cinemaSize: Pair<Int, Int>
        get() = _cinemaSize
        set(value) {
            _cinemaSize = value
            updateSeats()
        }

    private fun updateSeats() {
        val newSeats = List(cinemaSize.first) { MutableList(cinemaSize.second) { false } }
        for (key in seats.keys) {
            this.seats[key] = newSeats
        }
    }

    @Serializable
    var seats : MutableMap<Int, List<MutableList<Boolean>>> = mutableMapOf()

    override fun getSeats(filmId: Int) : Collection<Collection<Boolean>> = seats[filmId] ?: emptyList()

    override fun getSeat(filmId: Int, seat: Pair<Int, Int>) : Boolean = seats[filmId]?.get(seat.first)?.get(seat.second) ?: false

    override fun setSeat(filmId: Int, seat: Pair<Int, Int>, value: Boolean) {
        val seats = seats[filmId] ?: return
        seats[seat.first][seat.second] = value
    }

    override fun initSeats(filmId: Int) {
        seats[filmId] = List(cinemaSize.first) { MutableList(cinemaSize.second) { false } }
    }

    override fun remove(filmId: Int) {
        if (filmId !in seats.keys) {
            return
        }
        seats.remove(filmId)
    }

    override fun clear() {
        seats.clear()
    }

    override fun saveToFile() {
        val json = Json.encodeToString(seats)
        File(filePath).writeText(json)
    }

    override fun loadFromFile() {
        val json = File(filePath).readText()
        seats.clear()
        val newSeats = Json.decodeFromString<MutableMap<Int, List<MutableList<Boolean>>>>(json)
        if (newSeats.isNotEmpty()) {
            val key = newSeats.keys.first()
            cinemaSize = Pair(newSeats[key]!!.size, newSeats[key]!![0].size)
        }
        seats = newSeats
    }
}