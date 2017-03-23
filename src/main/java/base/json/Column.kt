package base.json

data class Column(
        val index: Int,
        var name: String = "default_name",
        val type: String = "default_type",
        val converters: MutableList<Converter> = mutableListOf()
)
