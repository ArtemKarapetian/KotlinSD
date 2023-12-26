package cinema.menu

import cinema.domain.controllers.interfaces.FilmControllerInterface
import cinema.domain.controllers.interfaces.TicketSeatsControllerInterface
import cinema.menu.cinemaInputHelper.CinemaInputHelper

class InfoMenu(
    private val filmController: FilmControllerInterface,
    private val ticketSeatsController: TicketSeatsControllerInterface,
) : MenuInterface {
    override var goBack = false

    override val menuItems = listOf(
        MenuItem("Изменить размер зала. Билеты сбросятся", ::changeCinemaSize),
        MenuItem("Сохранить данные", ::saveData),
        MenuItem("Загрузить данные", ::loadData),
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

    fun changeCinemaSize() {
        println("Введите новое количество рядов:")
        val rowSize = CinemaInputHelper.readPositiveInt()
        println("Введите новое количество мест в ряду:")
        val colSize = CinemaInputHelper.readPositiveInt()

        ticketSeatsController.changeCinemaSize(Pair(rowSize, colSize))
        println("Размер зала изменен")
    }

    fun saveData() {
        filmController.save()
        ticketSeatsController.save()
        println("Данные сохранены")
    }

    fun loadData() {
        filmController.load()
        ticketSeatsController.load()
        println("Данные загружены")
    }
}

        