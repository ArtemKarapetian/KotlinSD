package cinema.domain.controllers

import cinema.data.entity.Film
import cinema.data.interfaces.ObjectWithIdDaoInterface
import cinema.domain.controllers.interfaces.FilmControllerInterface
import cinema.domain.model.Error
import cinema.domain.model.OutputModel
import cinema.domain.validators.interfaces.FilmValidatorInterface
import java.time.LocalTime

class FilmController(
    private val filmDao: ObjectWithIdDaoInterface,
    private val filmValidator : FilmValidatorInterface
) : FilmControllerInterface {

    override fun getFilms(): OutputModel {
        val films = filmDao.getAll()
        return if (films.isEmpty()) {
            OutputModel("Нет фильмов в кинопрокате")
        } else {
            OutputModel(films.values.joinToString("\n"))
        }
    }

    override fun getFilmById(filmId: Int): OutputModel {
        val res = filmValidator.validateById(filmDao.getAll().keys, filmId)
        return if (res is Error) {
            OutputModel("Нет фильма с id $filmId")
        } else {
            OutputModel(filmDao.get(filmId).toString())
        }
    }

    override fun getFilmsByTitle(filmTitle: String): OutputModel {
        val films = filmDao.getAll().filterValues { (it as Film).title == filmTitle }.values
        return if (films.isEmpty()) {
            OutputModel("Нет фильмов с названием $filmTitle")
        } else {
            return OutputModel(films.joinToString("\n"))
        }
    }

    override fun addFilm(film: Film): OutputModel {
        val validationResult = filmValidator.validateFilm(filmDao.getAll().values as Collection<Film>, film)
        return if (validationResult is Error) {
            OutputModel(validationResult.message)
        } else {
            filmDao.add(film)
            OutputModel("Фильм добавлен")
        }
    }

    override fun addFilms(films: Collection<Film>): OutputModel {
        var validationResult = filmValidator.validateRange(films)
        if (validationResult) {
            return OutputModel("Фильмы накладываются друг на друга или выходят за границы дозволенного (начинаются между 23 и 9)")
        }
        validationResult = filmValidator.validateRange(films.plus(filmDao.getAll().values as Collection<Film>))
        if (validationResult) {
            return OutputModel("Фильмы накладываются на существующие фильмы")
        }
        for (film in films) {
            filmDao.add(film)
        }
        return OutputModel("Фильмы добавлены")
    }

    override fun changeFilmStart(filmId: Int, time: LocalTime): OutputModel {
        if (filmValidator.validateById(filmDao.getAll().keys, filmId) is Error) {
            return OutputModel("Нет фильма с id $filmId")
        }
        val prevFilm = (filmDao.get(filmId) as Film)
        val duration = prevFilm.endTime.hour * 60 + prevFilm.endTime.minute - prevFilm.startTime.hour * 60 - prevFilm.startTime.minute
        val newEndTime = time.plusMinutes(duration.toLong())
        val film = Film(filmId, prevFilm.title, time, newEndTime)
        val validationResult = filmValidator.validateFilm(filmDao.getAll().filter{ it.key != filmId }.values as Collection<Film>, film)
        if (validationResult is Error) {
            return OutputModel(validationResult.message)
        }
        filmDao.change(filmId, film)
        return OutputModel("Фильм изменен")
    }

    override fun changeFilmInfo(filmTitle: String, newFilmTitle: String) : OutputModel {
        val films = filmDao.getAll().filterValues { (it as Film).title == filmTitle }.values
        return if (films.isEmpty()) {
            OutputModel("Нет фильмов с названием $filmTitle")
        } else {
            for (film in films) {
                (film as Film).title = newFilmTitle
                filmDao.change(film.id, film)
            }
            OutputModel("Фильмы изменены")
        }
    }

    override fun changeFilmsDuration(filmTitle: String, duration: Int) : OutputModel {
        val films = filmDao.getAll().filterValues { (it as Film).title == filmTitle }.values.map { (it as Film).copy() } as Collection<Film>
        if (films.isEmpty()) {
            return OutputModel("Нет фильмов с названиями $filmTitle")
        }
        for (film in films) {
            film.endTime = film.startTime.plusMinutes(duration.toLong())
        }
        var validationResult = filmValidator.validateRange(films)
        if (validationResult) {
            return OutputModel("Фильмы изменены")
        }
        validationResult = filmValidator.validateRange(films.plus(filmDao.getAll().filter{ (it.value as Film).title != filmTitle}.values as Collection<Film>))
        if (validationResult) {
            return OutputModel("Фильмы накладываются с существующими фильмами")
        }
        for (film in films) {
            filmDao.change(film.id, film)
        }
        return OutputModel("Фильмы изменены")
    }

    override fun removeFilmById(filmId: Int): OutputModel {
        val res = filmValidator.validateById(filmDao.getAll().keys, filmId)
        return if (res is Error) {
            OutputModel("Нет фильма с id $filmId")
        } else {
            filmDao.remove(filmId)
            OutputModel("Фильм удалён")
        }
    }

    override fun clearFilms() = filmDao.clear()

    override fun save() = filmDao.saveToFile()

    override fun load() = filmDao.loadFromFile()
}