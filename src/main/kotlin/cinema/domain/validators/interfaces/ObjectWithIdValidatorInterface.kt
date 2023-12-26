package cinema.domain.validators.interfaces

import cinema.domain.model.Result
import cinema.data.entity.ObjectWithId

interface ObjectWithIdValidatorInterface {
    fun validateById(listIds: Collection<Int>, id: Int): Result
}