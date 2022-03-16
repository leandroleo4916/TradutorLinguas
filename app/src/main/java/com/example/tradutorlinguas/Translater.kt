package com.example.tradutorlinguas

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*

class Translator {

    @JvmStatic
    fun main(args: Array<String>) {
        val text = "Hello world!"
        //Translated text: Hallo Welt!
        println("Translated text: " + translate("en", "de", text))
    }

    fun translate(langFrom: String, langTo: String, text: String): String {

        val urlStr = "https://script.google.com/macros/s/AKfycbwEdjA_0xrRXhI-qyFwjCisfehoOgkCPAOR7Ovr5g/exec" +
                        "?q=" + URLEncoder.encode(text, "UTF-8") +
                        "&target=" + langTo + "&source=" + langFrom
        val url = URL(urlStr)
        val response = StringBuilder()
        val con = url.openConnection() as HttpURLConnection
        con.setRequestProperty("User-Agent", "Mozilla/5.0")
        val input = BufferedReader(InputStreamReader(con.getInputStream()))
        var inputLine: String?

        while (input.readLine().also { inputLine = it } != null) {
            response.append(inputLine)
        }
        input.close()
        return response.toString()
    }

    val language: ArrayList<String> = arrayListOf("Árabe", "Bósnio", "Búlgaro", "Tcheco",
            "Dinamarquês", "Holandês", "Inglês", "Finlandês", "Francês", "Alemão", "Grego",
            "Irlandês", "Japonês", "Coreano", "Polonês", "Português", "Romena", "Russo",
            "Espanhol", "Sueco", "Turco")

    val code: ArrayList<String> = arrayListOf("ar", "bs", "bg", "cs", "da", "nl", "en", "fi", "fr",
            "de", "el", "ga", "ja", "ko", "pl", "pt", "ro", "ru", "es", "sv", "tr")

    enum class Language(val str: String) {
        Inglês("en"),
        Português("pt");
    }
}