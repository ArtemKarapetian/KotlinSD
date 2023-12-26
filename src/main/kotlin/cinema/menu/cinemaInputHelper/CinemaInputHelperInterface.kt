package cinema.menu.cinemaInputHelper

import cinema.data.entity.Film
import java.time.LocalTime

interface CinemaInputHelperInterface {
    fun readPositiveInt(): Int
    fun readIntRange(range: Int): Int
    fun readString(): String
    fun readTime(): LocalTime
    fun readFilmSessions(): List<Film>
}