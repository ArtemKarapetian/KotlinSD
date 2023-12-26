package cinema.data

import cinema.data.entity.ObjectWithId
import cinema.data.entity.Ticket
import cinema.data.interfaces.ObjectWithIdDaoInterface
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.*

class TicketDao : ObjectWithIdDaoInterface {
    override var filePath: String = "src/main/resources/tickets.json"
    protected var idCounter = 1
    @Serializable
    protected val tickets = mutableMapOf<Int, Ticket>()

    override fun add(obj: ObjectWithId) {
        (obj as Ticket).id = idCounter
        tickets[idCounter++] = obj as Ticket 
    }

    override fun get(id: Int) : Ticket? {
        return tickets[id]
    }

    override fun change(id: Int, obj: ObjectWithId) {
        if (obj !is Ticket) {
            return
        }
        val prevId = tickets[id]?.id ?: return
        tickets[id] = obj
        tickets[id]?.id = prevId
    }

    override fun getAll() : Map<Int, Ticket> {
        return Collections.unmodifiableMap(tickets)
    }

    override fun remove(id: Int) {
        tickets.remove(id)
    }

    override fun clear() {
        tickets.clear()
    }

    override fun saveToFile() {
        val json = Json.encodeToString(tickets)
        File(filePath).writeText(json)
    }

    override fun loadFromFile() {
        val json = File(filePath).readText()
        tickets.clear()
        val newTickets = Json.decodeFromString<MutableMap<Int, Ticket>>(json)
        var maxId = 0
        for (key in newTickets.keys) {
            tickets[key] = newTickets[key]!!
            maxId = maxOf(maxId, key)
        }
        idCounter = maxId + 1
    }
}