package cinema.domain.controllers.interfaces

import cinema.data.entity.Film
import cinema.domain.model.OutputModel
import java.time.LocalTime

interface FilmControllerInterface {
    fun getFilms(): OutputModel
    fun getFilmById(filmId: Int): OutputModel
    fun getFilmsByTitle(filmTitle: String): OutputModel

    fun addFilm(film: Film) : OutputModel
    fun addFilms(films: Collection<Film>): OutputModel

    fun changeFilmStart(filmId: Int, time: LocalTime): OutputModel
    fun changeFilmInfo(filmTitle: String, newFilmTitle: String) : OutputModel
    fun changeFilmsDuration(filmTitle: String, duration: Int) : OutputModel

    fun removeFilmById(filmId: Int) : OutputModel

    fun clearFilms()

    fun save()
    fun load()
}