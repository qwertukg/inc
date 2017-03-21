package strings.converters

import base.json.*

class ToUpper(value: String, parameters: MutableList<Parameter>? = null) : Converter(value, parameters) {
    override var name: String = "to_upper"

    override fun convert(): ToUpper {
        value = value.toUpperCase()

        return this
    }
}
