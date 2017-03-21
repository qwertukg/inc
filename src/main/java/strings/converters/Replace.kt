package strings.converters

import base.json.*

class Replace(value: String, parameters: MutableList<Parameter>? = null) : Converter(value, parameters) {
    override var name: String = "replace"

    override fun convert(): Replace {
        parameters?.forEach {
            value = value.replace(it.first, it.second)
        }

        return this
    }
}
