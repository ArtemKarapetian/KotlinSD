package cinema.menu

interface MenuInterface {
    var goBack: Boolean
    val menuItems: List<MenuItem>
    fun show()
    fun back() {
        goBack = true
    }
}