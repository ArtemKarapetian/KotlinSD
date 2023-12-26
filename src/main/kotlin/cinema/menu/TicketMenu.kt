package cinema.menu

import cinema.data.entity.Ticket
import cinema.domain.controllers.interfaces.FilmControllerInterface
import cinema.domain.controllers.interfaces.TicketSeatsControllerInterface
import cinema.menu.cinemaInputHelper.CinemaInputHelper

class TicketMenu(
    private val filmController: FilmControllerInterface,
    private val ticketSeatsController: TicketSeatsControllerInterface,
): MenuInterface {
    override var goBack = false

    override val menuItems = listOf(
        MenuItem("Купить билет", ::buyTicket),
        MenuItem("Показать все билеты", ::showAllTickets),
        MenuItem("Вернуть билет", ::returnTicket),
        MenuItem("Показать все свободные места на сеанс", ::showFilmSessionSeats),
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

    fun buyTicket() {
        println("Список сеансов:")
        println(filmController.getFilms().value)
        if (filmController.getFilms().value == "Нет фильмов в кинопрокате") {
            return
        }
        println("Введите id сеанса:")
        val filmSessionId = CinemaInputHelper.readPositiveInt()
        println("Введите кол-во мест:")
        val seatsCount = CinemaInputHelper.readIntRange(ticketSeatsController.getSeatsCount())
        val seats = mutableListOf<Pair<Int, Int>>()
        for (i in 1..seatsCount) {
            println("Введите номер ряда:")
            val rowNumber = CinemaInputHelper.readIntRange(ticketSeatsController.getRowNumber())
            println("Введите номер места:")
            val seatNumber = CinemaInputHelper.readIntRange(ticketSeatsController.getSeatNumber())
            seats.add(Pair(rowNumber, seatNumber))
        }
        val message = ticketSeatsController.handleTicket(Ticket(0, filmSessionId, seats))
        println(message.value)
    }

    fun returnTicket() {
        println("Список билетов:")
        println(ticketSeatsController.getTickets().value)
        if (ticketSeatsController.getTickets().value == "Нет билетов в кинопрокате") {
            return
        }
        println("Введите id билета для возврата:")
        val ticketId = CinemaInputHelper.readPositiveInt()
        val message = ticketSeatsController.returnTicket(ticketId)
        println(message.value)
    }

    fun showAllTickets() {
        println(ticketSeatsController.getTickets().value)
    }

    fun showFilmSessionSeats() {
        println("Список сеансов:")
        println(filmController.getFilms().value)
        if (filmController.getFilms().value == "Нет фильмов в кинопрокате") {
            return
        }
        println("Введите id сеанса:")
        val filmSessionId = CinemaInputHelper.readPositiveInt()
        println(ticketSeatsController.getSeats(filmSessionId).value)
    }
}