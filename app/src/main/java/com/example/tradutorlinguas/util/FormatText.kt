package com.example.tradutorlinguas.util

class FormatText {

    fun formatText(string: String): String {
        val s = string.split(" ")
        var str = ""

        for (n in s.indices) {
            str += if (s[n].contains("&#39;")) {
                if (n == 0) {
                    s[n].replaceFirst("&#39;", "'")
                } else {
                    " " + s[n].replaceFirst("&#39;", "'")
                }
            } else {
                if (n == 0) {
                    s[n]
                } else " " + s[n]
            }
        }
        return str
    }
}