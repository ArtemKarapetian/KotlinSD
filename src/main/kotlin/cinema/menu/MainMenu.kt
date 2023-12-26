package cinema.menu

import cinema.domain.controllers.interfaces.FilmControllerInterface
import cinema.domain.controllers.interfaces.TicketSeatsControllerInterface
import cinema.menu.cinemaInputHelper.CinemaInputHelper

class MainMenu(
    private val filmController: FilmControllerInterface,
    private val ticketSeatsController: TicketSeatsControllerInterface,
    private val filmMenu: MenuInterface,
    private val ticketMenu: MenuInterface,
    private val infoMenu: MenuInterface,
) : MenuInterface {
    override var goBack = false

    override val menuItems = listOf(
        MenuItem("Меню фильмов", ::openFilmMenu),
        MenuItem("Меню билетов", ::openTicketMenu),
        MenuItem("Взаимодействие с данными кинотеатра", ::openInfoMenu),
        MenuItem("Сохранить и выйти", ::saveAndExit)
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

    fun openFilmMenu() = filmMenu.show()

    fun openTicketMenu() = ticketMenu.show()

    fun openInfoMenu() = infoMenu.show()

    fun saveAndExit() {
        filmController.save()
        ticketSeatsController.save()
        println("Данные сохранены. До свидания!")
        goBack = true
    }
}