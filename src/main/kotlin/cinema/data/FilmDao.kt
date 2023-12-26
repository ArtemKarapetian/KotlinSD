package cinema.data

import cinema.data.entity.Film
import cinema.data.entity.ObjectWithId
import cinema.data.interfaces.ObjectWithIdDaoInterface
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.*

class FilmDao : ObjectWithIdDaoInterface {
    override var filePath: String = "src/main/resources/films.json"

    protected var idCounter = 1
    @Serializable
    protected val films = mutableMapOf<Int, Film>()

    override fun add(obj: ObjectWithId) {
        (obj as Film).id = idCounter
        films[idCounter++] = obj
    }

    override fun get(id: Int) : Film? {
        return films[id]
    }

    override fun change(id: Int, obj: ObjectWithId) {
        if (obj !is Film) {
            return
        }
        val prevId = films[id]?.id ?: return
        films[id] = obj
        films[id]?.id = prevId
    }

    override fun getAll() : Map<Int, Film> {
        return Collections.unmodifiableMap(films)
    }

    override fun remove(id: Int) {
        films.remove(id)
    }

    override fun clear() {
        films.clear()
    }

    override fun saveToFile() {
        val json = Json.encodeToString(films)
        File(filePath).writeText(json)
    }

    override fun loadFromFile() {
        val json = File(filePath).readText()
        films.clear()
        val newFilms = Json.decodeFromString<MutableMap<Int, Film>>(json)
        var maxId = 0
        for (key in newFilms.keys) {
            films[key] = newFilms[key]!!
            maxId = maxOf(maxId, key)
        }
        idCounter = maxId + 1
    }
}