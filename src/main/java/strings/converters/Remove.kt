package strings.converters

import base.json.*

class Remove(value: String, parameters: MutableList<Parameter>? = null) : Converter(value, parameters) {
    override var name: String = "remove"

    override fun convert(): Remove {
        parameters?.forEach {
            value = value.replace(it.first, "")
        }

        return this
    }
}
