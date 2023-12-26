package cinema.di

import cinema.data.FilmDao
import cinema.data.SeatsDao
import cinema.data.TicketDao
import cinema.data.interfaces.ObjectWithIdDaoInterface
import cinema.data.interfaces.SeatsDaoInterface
import cinema.domain.controllers.FilmController
import cinema.domain.controllers.TicketSeatsController
import cinema.domain.controllers.interfaces.FilmControllerInterface
import cinema.domain.controllers.interfaces.TicketSeatsControllerInterface
import cinema.domain.validators.FilmValidator
import cinema.domain.validators.TicketSeatsValidator
import cinema.domain.validators.interfaces.FilmValidatorInterface
import cinema.domain.validators.interfaces.TicketSeatsValidatorInterface
import cinema.menu.MenuInterface
import cinema.menu.FilmMenu
import cinema.menu.TicketMenu
import cinema.menu.InfoMenu
import cinema.menu.MainMenu


object DI {
    private val filmValidator: FilmValidatorInterface
        get() = FilmValidator()

    private val filmDao: ObjectWithIdDaoInterface by lazy {
        FilmDao()
    }

    private val ticketValidator: TicketSeatsValidatorInterface
        get() = TicketSeatsValidator()

    private val ticketDao: ObjectWithIdDaoInterface by lazy {
        TicketDao()
    }

    private val seatsDao: SeatsDaoInterface by lazy {
        SeatsDao()
    }

    val filmController: FilmControllerInterface
        get() = FilmController(
            filmValidator = filmValidator,
            filmDao = filmDao,
        )

    val ticketSeatsController: TicketSeatsControllerInterface
        get() = TicketSeatsController(
            ticketValidator = ticketValidator,
            ticketDao = ticketDao,
            seatsDao = seatsDao,
        )

    private val filmMenu: MenuInterface by lazy {
        FilmMenu(
            filmController = filmController,
            ticketSeatsController = ticketSeatsController,
        )
    }

    private val ticketMenu: MenuInterface by lazy {
        TicketMenu(
            filmController = filmController,
            ticketSeatsController = ticketSeatsController,
        )
    }

    private val infoMenu: MenuInterface by lazy {
        InfoMenu(
            filmController = filmController,
            ticketSeatsController = ticketSeatsController,
        )
    }

    private val mainMenu: MenuInterface by lazy {
        MainMenu(
            filmController = filmController,
            ticketSeatsController = ticketSeatsController,
            filmMenu = filmMenu,
            ticketMenu = ticketMenu,
            infoMenu = infoMenu,
        )
    }

    fun start() {
        mainMenu.show()
    }
}
