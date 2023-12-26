package cinema.data.interfaces

interface SeatsDaoInterface : SerializeInterface {
    var cinemaSize: Pair<Int, Int>
    fun getSeats(filmId: Int) : Collection<Collection<Boolean>>
    fun getSeat(filmId: Int, seat: Pair<Int, Int>) : Boolean
    fun setSeat(filmId: Int, seat: Pair<Int, Int>, value: Boolean)
    fun initSeats(filmId: Int)
    fun remove(filmId: Int)
    fun clear()
}