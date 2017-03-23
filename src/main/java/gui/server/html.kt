package gui.server

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.apache.commons.csv.*

fun renderPage(cols: Int, csvColor: String, jsonColor: String, runColor: String): String {
    return createHTML().html {
        head {
            styleLink("https://cdn.datatables.net/1.10.13/css/jquery.dataTables.min.css")
            style {
                +"pre.source { border-bottom:1px dotted #f66; display:inline; }"
                +"pre.result { border-bottom:1px dotted #666; display:inline; }"
            }
            script(ScriptType.textJScript, "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.0/jquery.min.js")
            script(ScriptType.textJScript, "https://cdn.datatables.net/1.10.13/js/jquery.dataTables.min.js")
            script(ScriptType.textJScript, "/data-tables.js")
        }
        body {
            attributes["style"] = "font-family:arial; font-size:13.3333px; color:#333;"
            div("container-fluid") {
                div("row") {
                    div("col-md-12") {
                        bootstrapPanel("Data input") {
                            postForm("/csv", FormEncType.multipartFormData) {
                                attributes["style"] = "float:left;"
                                bootstrapInput("csv", csvColor)
                            }
                            postForm("/json", FormEncType.multipartFormData) {
                                attributes["style"] = "float:left;"
                                bootstrapInput("json", jsonColor)
                            }
                            postForm("/run") {
                                attributes["style"] = "float:left;"
                                submitInput {
                                    attributes["style"] = "color:$runColor;"
                                    value = "Run"
                                }
                            }
                            div { attributes["style"] = "clear:left;" }
                        }
                    }
                }

                div("row") {
                    div("col-md-12") {
                        bootstrapPanel("Results") {
                            table("data display") {
                                attributes["style"] = "font-family:monospace;"
                                attributes["cellspacing"] = "0"
                                attributes["width"] = "100%"
                                thead {
                                    tr {
                                        th {
                                            attributes["style"] = "text-align:left;"
                                            +"#"
                                        }
                                        for (i in 1..cols) {
                                            th {
                                                attributes["style"] = "text-align:left;"
                                                +i.toString()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

private fun FORM.bootstrapInput(_name: String, color: String) {
    submitInput {
        attributes["style"] = "float:left; margin-right:2px; color:$color;"
        value = "Load ${_name.capitalize()}"
    }
    div("form-group") {
        attributes["style"] = "float:left;"

        fileInput {
            attributes["id"] = _name
            name = _name
        }
    }
}

private fun DIV.bootstrapPanel(title: String, block: DIV.() -> Unit) {
    div {
        attributes["style"] = "margin:10px 0; border-bottom:1px dotted #111;"
        +title.toUpperCase()
    }
    block()
}
