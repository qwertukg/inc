package base.json

import base.Convertable

open class Converter(value: String, override val parameters: MutableList<Parameter>? = null) : Convertable {
    override var name: String = "default"
    override var source = value

    override var value = value
        set(value) {
            if (field != value) {

            }

            field = value
        }

    override fun convert(): Convertable {
        return this
    }
}
