package cinema.data.interfaces

import cinema.data.entity.*

interface ObjectWithIdDaoInterface : SerializeInterface {
    fun add(obj: ObjectWithId)
    fun get(id: Int) : ObjectWithId?
    fun change(id: Int, obj: ObjectWithId)
    fun getAll() : Map<Int, ObjectWithId> 
    fun remove(id: Int)
    fun clear()
}