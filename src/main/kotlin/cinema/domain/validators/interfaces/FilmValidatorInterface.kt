package cinema.domain.validators.interfaces

import cinema.data.entity.Film
import cinema.domain.model.Result

interface FilmValidatorInterface : ObjectWithIdValidatorInterface {
    fun validateFilm(listFilms: Collection<Film>, film: Film): Result
    fun validateRange(listFilms: Collection<Film>): Boolean
}