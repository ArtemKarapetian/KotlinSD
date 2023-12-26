package cinema.menu.cinemaInputHelper

import cinema.data.entity.Film
import java.time.LocalTime
import java.util.*

object CinemaInputHelper : CinemaInputHelperInterface {
    private val scanner = Scanner(System.`in`)

    override fun readPositiveInt(): Int {
        var input: String

        while (true) {
            input = scanner.nextLine()
            try {
                if (input.toInt() <= 0) {
                    println("Неправильный ввод. Пожалуйста, введите положительное число.")
                } else {
                    return input.toInt()
                }
            } catch (e: NumberFormatException) {
                println("Неправильный ввод. Пожалуйста, введите число.")
            }
        }
    }

    override fun readIntRange(range: Int): Int {
        var input: String

        while (true) {
            input = scanner.nextLine()
            try {
                if (input.toInt() in 1..range) {
                    return input.toInt()
                } else {
                    println("Неправильный ввод. Пожалуйста, введите число от 1 до $range.")
                }
            } catch (e: NumberFormatException) {
                println("Неправильный ввод. Пожалуйста, введите число.")
            }
        }
    }

    override fun readString(): String {
        var input: String

        do {
            input = scanner.nextLine()
        } while (input.isEmpty())

        return input
    }

    override fun readTime() : LocalTime {
        var input: String

        while (true) {
            input = scanner.nextLine()
            try {
                val hours = input.substringBefore(":").toInt()
                val minutes = input.substringAfter(":").toInt()
                if (hours in 0..23 && minutes in 0..59) {
                    return LocalTime.of(hours, minutes)
                } else {
                    println("Неправильный ввод. Пожалуйста, введите время в формате ЧЧ:ММ.")
                }
            } catch (e: NumberFormatException) {
                println("Неправильный ввод. Пожалуйста, введите время в формате ЧЧ:ММ.")
            }
        }
    }

    override fun readFilmSessions(): List<Film> {
        println("Введите название фильма:")
        val name = readString()
        println("Введите количество сеансов фильма:")
        val sessionsCount = readIntRange(100)
        println("Введите продолжительность фильма в минутах:")
        val duration = readIntRange(1000)
        println("Вводите время начала сеанса в формате ЧЧ:ММ :")
        val sessions = MutableList(sessionsCount) { LocalTime.of(0, 0) }
        for (i in 0 until sessionsCount) {
            println("Введите время начала сеанса №${i + 1}:")
            sessions[i] = readTime()
        }
        val listFilms: MutableList<Film> = mutableListOf()
        for (i in 0 until sessionsCount) {
            listFilms.add(Film(0, name, sessions[i], sessions[i].plusMinutes(duration.toLong())))
        }
        return listFilms
    }
}
