package base.json

import base.Convertable

open class Converter(override var value: String = "_value", override val parameters: MutableList<Parameter>? = null) : Convertable {
    override var name = "_name"

    override fun convert(): Convertable {
        return this
    }
}
