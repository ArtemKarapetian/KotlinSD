package cinema.domain.validators

import cinema.data.entity.Ticket
import cinema.domain.model.Error
import cinema.domain.model.Result
import cinema.domain.model.Success
import cinema.domain.validators.interfaces.TicketSeatsValidatorInterface

class TicketSeatsValidator : TicketSeatsValidatorInterface {
    override fun validateTicket(seats: List<List<Boolean>>, ticket: Ticket, value: Boolean): Result {
        return when {
            ticket.seats.isEmpty() -> Error("В билете нет мест")
            ticket.seats.any { it.first <= 0 || it.first > seats.size || it.second <= 0 || it.second > seats[0].size } -> Error("Номер места вышел за границы дозволенного")
            ticket.seats.any { seats[it.first - 1][it.second - 1] != value } -> when (value) {
                true -> Error("Место, указанное в билете, уже свободно")
                false -> Error("Место, указанное в билете, уже куплено")
            }
            else -> Success
        }
    }

    override fun validateById(listIds: Collection<Int>, id: Int): Result {
        return when {
            !listIds.contains(id) -> Error("Не существует билета с таким id")
            else -> Success
        }
    }
}