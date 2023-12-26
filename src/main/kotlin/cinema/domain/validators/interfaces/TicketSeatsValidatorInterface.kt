package cinema.domain.validators.interfaces

import cinema.domain.model.Result
import cinema.data.entity.Ticket

interface TicketSeatsValidatorInterface : ObjectWithIdValidatorInterface {
    fun validateTicket(seats: List<List<Boolean>>, ticket: Ticket, value: Boolean): Result
}