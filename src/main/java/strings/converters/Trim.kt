package strings.converters

import base.json.*

class Trim(value: String, parameters: MutableList<Parameter>? = null) : Converter(value, parameters) {
    override var name: String = "trim"

    override fun convert(): Trim {
        value = value.trim()

        return this
    }
}
