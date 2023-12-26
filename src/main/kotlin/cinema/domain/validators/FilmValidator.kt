package cinema.domain.validators

import cinema.data.entity.Film
import cinema.domain.model.Error
import cinema.domain.model.Result
import cinema.domain.model.Success
import cinema.domain.validators.interfaces.FilmValidatorInterface
import java.time.LocalTime

class FilmValidator : FilmValidatorInterface {
    override fun validateFilm(listFilms: Collection<Film>, film: Film): Result {
        return when {
            film.title.isEmpty() -> Error("Название пусто")
            film.startTime.isAfter(film.endTime) -> Error("Начало фильма после его конца")
            film.startTime.isBefore(LocalTime.of(9, 0)) -> Error("Начала фильма слишком рано")
            film.endTime.isAfter(LocalTime.of(23, 0)) -> Error("Конец фильма слишком поздно")
            checkForOverlap(listFilms, film) -> Error("Фильм накладывается на уже существующие")
            else -> Success
        }
    }

    fun checkForOverlap(listFilms: Collection<Film>, film: Film): Boolean {
        return listFilms.any {
                (film.startTime.isAfter(it.startTime) && film.startTime.isBefore(it.endTime) ||
                film.endTime.isAfter(it.startTime) && film.endTime.isBefore(it.endTime) ||
                film.startTime.isBefore(it.startTime) && film.endTime.isAfter(it.endTime))
        }
    }

    override fun validateRange(listFilms: Collection<Film>): Boolean {
        val timeBorder = listFilms.any {
            it.startTime.isBefore(LocalTime.of(9, 0)) ||
            it.endTime.isAfter(LocalTime.of(23, 0))
        }
        if (timeBorder) {
            return true
        }
        val sortedFilms = listFilms.sortedBy { it.startTime }
        for (i in 0 until sortedFilms.size - 1) {
            if (sortedFilms[i].endTime.isAfter(sortedFilms[i + 1].startTime)) {
                return true
            }
        }
        return false
    }

    override fun validateById(listIds: Collection<Int>, id: Int): Result {
        return when {
            !listIds.contains(id) -> Error("There is no such film")
            else -> Success
        }
    }
}