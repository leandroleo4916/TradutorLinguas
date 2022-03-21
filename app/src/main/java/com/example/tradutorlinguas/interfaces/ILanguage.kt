package com.example.tradutorlinguas.interfaces

import com.example.tradutorlinguas.dataclass.LanguageData
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


    suspend fun resulted(language: LanguageData): String{
        val urlStr = "https://script.google.com/macros/s/AKfycbwEdjA_0xrRXhI-qyFwjCisfehoOgkCPAOR7Ovr5g/exec" +
                "?q=" + URLEncoder.encode(language.text, "UTF-8") +
                "&target=" + language.to + "&source=" + language.from
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
        return response.toString()
    }
