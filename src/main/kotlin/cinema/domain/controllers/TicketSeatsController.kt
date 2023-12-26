package cinema.domain.controllers

import cinema.data.entity.Ticket
import cinema.data.interfaces.ObjectWithIdDaoInterface
import cinema.data.interfaces.SeatsDaoInterface
import cinema.domain.controllers.interfaces.TicketSeatsControllerInterface
import cinema.domain.model.Error
import cinema.domain.model.OutputModel
import cinema.domain.validators.interfaces.TicketSeatsValidatorInterface

class TicketSeatsController(
    private val ticketDao: ObjectWithIdDaoInterface,
    private val seatsDao: SeatsDaoInterface,
    private val ticketValidator: TicketSeatsValidatorInterface
) : TicketSeatsControllerInterface {

    override fun getSeatsCount(): Int {
        return seatsDao.cinemaSize.first * seatsDao.cinemaSize.second
    }

    override fun getRowNumber(): Int {
        return seatsDao.cinemaSize.first
    }

    override fun getSeatNumber(): Int {
        return seatsDao.cinemaSize.second
    }

    override fun getSeats(filmId: Int): OutputModel {
        val seats = seatsDao.getSeats(filmId)
        return if (seats.isEmpty()) {
            OutputModel("Нет билета с id $filmId")
        } else {
            val formattedSeats = seats.joinToString("\n") { row ->
                row.joinToString(" ") { seat ->
                    if (seat) "X" else "O"
                }
            }
            OutputModel(formattedSeats)
        }
    }

    override fun initFilmSeats(filmId: Int) {
        seatsDao.initSeats(filmId)
    }

    override fun changeCinemaSize(newSize: Pair<Int, Int>) {
        seatsDao.cinemaSize = newSize
        ticketDao.clear()
    }

    override fun getTickets(): OutputModel {
        val tickets = ticketDao.getAll().values
        return if (tickets.isEmpty()) {
            OutputModel("Нет билетов в кинопрокате")
        } else {
            OutputModel(tickets.joinToString("\n"))
        }
    }

    override fun handleTicket(ticket: Ticket): OutputModel {
        val validationResult = ticketValidator.validateTicket(seatsDao.getSeats(ticket.filmId).map { it.toList() }, ticket, false)
        return if (validationResult is Error) {
            OutputModel(validationResult.message)
        } else {
            ticketDao.add(ticket)
            for (seat in ticket.seats) {
                seatsDao.setSeat(ticket.filmId, Pair(seat.first - 1, seat.second - 1), true)
            }
            OutputModel("Билет куплен")
        }
    }

    override fun returnTicket(ticketId: Int): OutputModel {
        val res = ticketValidator.validateById(ticketDao.getAll().keys, ticketId)
        return if (res is Error) {
            OutputModel("Нет билета с id $ticketId")
        } else {
            ticketDao.remove(ticketId)
            OutputModel("Билет возвращён")
        }
    }

    override fun removeTicketsById(filmId: Int) {
        val res = ticketDao.getAll().filter { (it.value as Ticket).filmId == filmId }
        for (ticket in res) {
            ticketDao.remove(ticket.key)
        }
        seatsDao.remove(filmId)
    }

    override fun clearTickets() = ticketDao.clear()

    override fun save() {
        ticketDao.saveToFile()
        seatsDao.saveToFile()
    }

    override fun load() {
        ticketDao.loadFromFile()
        seatsDao.loadFromFile()
    }
}