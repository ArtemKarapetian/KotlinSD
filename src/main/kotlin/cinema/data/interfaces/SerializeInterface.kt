package cinema.data.interfaces

interface SerializeInterface {
    var filePath: String
    fun saveToFile()
    fun loadFromFile()
}