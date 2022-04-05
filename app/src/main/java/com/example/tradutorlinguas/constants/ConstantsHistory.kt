package com.example.tradutorlinguas.constants

class ConstantsHistory private constructor(){

    object HISTORY {
        const val TABLE_NAME = "history"
        object COLUMNS {
            const val ID = "id"
            const val LANGFROM = "langfrom"
            const val LANGTO = "langto"
            const val TEXTFROM = "textfrom"
            const val TEXTTO = "textto"
        }
    }
}