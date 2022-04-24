package com.example.tradutorlinguas.remote

import com.example.tradutorlinguas.dataclass.LanguageData
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class Translator {

    fun translate(lang: LanguageData): String {
        return try {
            val urlStr =
                    "https://script.google.com/macros/s/AKfycbwEdjA_0xrRXhI-qyFwjCisfehoOgkCPAOR7Ovr5g/exec" +
                            "?q=" + URLEncoder.encode(lang.textFrom, "UTF-8") +
                            "&target=" + lang.to + "&source=" + lang.from
            val url = URL(urlStr)
            val response = StringBuilder()
            val con = url.openConnection() as HttpURLConnection
            con.setRequestProperty("User-Agent", "Mozilla/5.0")

            val input = BufferedReader(InputStreamReader(con.inputStream))
            var inputLine: String?
            while (input.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            input.close()
            response.toString()

        } catch (e: Exception) {
            "Erro na tradução, tente novamente!"
        }
    }
}
