package cinema.menu

import cinema.domain.controllers.interfaces.FilmControllerInterface
import cinema.domain.controllers.interfaces.TicketSeatsControllerInterface
import cinema.menu.cinemaInputHelper.CinemaInputHelper

class FilmMenu(
    private val filmController: FilmControllerInterface,
    private val ticketSeatsController: TicketSeatsControllerInterface,
): MenuInterface {
    override var goBack = false

    override val menuItems = listOf(
        MenuItem("Добавить фильм", ::addFilm),
        MenuItem("Изменить время начала фильма", ::changeFilmStart),
        MenuItem("Изменить название фильма", ::changeFilmInfo),
        MenuItem("Изменить продолжительность фильма", ::changeFilmsDuration),
        MenuItem("Удалить фильм", ::deleteFilmById),
        MenuItem("Показать все фильмы", ::showAllFilms),
        MenuItem("Найти фильм по названию", ::findFilmByName),
        MenuItem("Назад", ::back)
    )

    override fun show() {
        while (!goBack) {
            for ((index, item) in menuItems.withIndex()) {
                println("${index + 1}. ${item.name}")
            }
            val actionNum = CinemaInputHelper.readIntRange(menuItems.size)
            menuItems[actionNum - 1].action()
        }
        goBack = false
    }

    fun addFilm() {
        val films = CinemaInputHelper.readFilmSessions()
        if (films.isEmpty()) {
            println("Неверный ввод")
            return
        }
        if (films.size == 1) {
            val message = filmController.addFilm(films[0])
            println(message.value)
            return
        }
        val message = filmController.addFilms(films)
        println(message.value)
        for (film in films) {
            ticketSeatsController.initFilmSeats(film.id)
        }
    }

    fun changeFilmStart() {
        println("Список фильмов:")
        println(filmController.getFilms().value)
        println("Введите id фильма для изменения времени начала:")
        val filmId = CinemaInputHelper.readPositiveInt()
        println("Введите новое время начала фильма:")
        val time = CinemaInputHelper.readTime()
        val message = filmController.changeFilmStart(filmId, time)
        println(message.value)
    }

    fun changeFilmInfo() {
        println("Список фильмов:")
        println(filmController.getFilms().value)
        println("Введите название фильма, чьё название вы хотите изменить:")
        val filmTitle = CinemaInputHelper.readString()
        if (filmController.getFilmsByTitle(filmTitle).value == "Нет фильмов с названием $filmTitle") {
            println("Нет фильмов с названием $filmTitle")
            return
        }
        println("Введите новое название фильма:")
        val newFilmTitle = CinemaInputHelper.readString()
        filmController.changeFilmInfo(filmTitle, newFilmTitle)
    }

    fun changeFilmsDuration() {
        println("Список фильмов:")
        println(filmController.getFilms().value)
        println("Введите название фильма, чьё название вы хотите изменить:")
        val filmTitle = CinemaInputHelper.readString()
        if (filmController.getFilmsByTitle(filmTitle).value == "Нет фильмов с названием $filmTitle") {
            println("Нет фильмов с названием $filmTitle")
            return
        }
        println("Введите новую продолжительность фильмов:")
        val duration = CinemaInputHelper.readPositiveInt()
        val message = filmController.changeFilmsDuration(filmTitle, duration)
        println(message.value)
    }

    fun deleteFilmById() {
        println("Список фильмов:")
        println(filmController.getFilms().value)
        println("Введите id фильма для удаления:")
        val filmId = CinemaInputHelper.readPositiveInt()
        val message = filmController.removeFilmById(filmId)
        println(message.value)
        if (message.value != "Фильм удалён") {
            return
        }
        ticketSeatsController.removeTicketsById(filmId)
    }

    fun showAllFilms() {
        println(filmController.getFilms().value)
    }

    fun findFilmByName() {
        println("Введите название фильма:")
        val filmTitle = CinemaInputHelper.readString()
        println(filmController.getFilmsByTitle(filmTitle).value)
    }
}

        