package base.json

import base.Convertable
import base.Selectable

class Field(override val x: Int, override val y: Int, var value: String) : Selectable {
    override val name: String = "_name"
    override val type: String = "_type"
    val source: String = value

    val converters: MutableList<Convertable> = mutableListOf()

}
