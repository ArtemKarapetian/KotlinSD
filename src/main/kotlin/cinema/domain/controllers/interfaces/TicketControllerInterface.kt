package cinema.domain.controllers.interfaces

import cinema.data.entity.Ticket
import cinema.domain.model.OutputModel

interface TicketSeatsControllerInterface {
    fun getSeatsCount() : Int
    fun getRowNumber() : Int
    fun getSeatNumber() : Int
    fun getSeats(filmId: Int) : OutputModel

    fun initFilmSeats(filmId: Int)

    fun changeCinemaSize(newSize: Pair<Int, Int>)

    fun getTickets() : OutputModel
    fun handleTicket(ticket: Ticket) : OutputModel
    fun returnTicket(ticketId: Int) : OutputModel
    fun removeTicketsById(filmId: Int)

    fun clearTickets()

    fun save()
    fun load()
}