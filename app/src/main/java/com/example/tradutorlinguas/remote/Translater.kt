package com.example.tradutorlinguas

import android.widget.TextView
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*
import androidx.lifecycle.liveData
import com.example.tradutorlinguas.dataclass.LanguageData
import com.example.tradutorlinguas.interfaces.ILanguage

class Translator {

    fun translate(lang: LanguageData, textTo: TextView) {

        val urlStr = "https://script.google.com/macros/s/AKfycbwEdjA_0xrRXhI-qyFwjCisfehoOgkCPAOR7Ovr5g/exec" +
                        "?q=" + URLEncoder.encode(lang.text, "UTF-8") +
                        "&target=" + lang.to + "&source=" + lang.from
        val url = URL(urlStr)
        val response = StringBuilder()
        val con = url.openConnection() as HttpURLConnection
        con.setRequestProperty("User-Agent", "Mozilla/5.0")

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {

                val input = BufferedReader(InputStreamReader(con.inputStream))
                var inputLine: String?
                while (input.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                input.close()
                textTo.text = response
            }
        }

    }

    val language: ArrayList<String> = arrayListOf("Árabe", "Bósnio", "Búlgaro", "Tcheco",
            "Dinamarquês", "Holandês", "Inglês", "Finlandês", "Francês", "Alemão", "Grego",
            "Irlandês", "Japonês", "Coreano", "Polonês", "Português", "Romena", "Russo",
            "Espanhol", "Sueco", "Turco")

    val code: ArrayList<String> = arrayListOf("ar", "bs", "bg", "cs", "da", "nl", "en", "fi", "fr",
            "de", "el", "ga", "ja", "ko", "pl", "pt", "ro", "ru", "es", "sv", "tr")

    enum class Language(val str: String) {
        Inglês("en"),
        Português("pt"),
        Espanhol("es"),
        Françês("fr"),
        Alemão("de"),
        Árabe("ar"),
        Bósnio("bs"),
        Búlgaro("bg"),
        Tcheco("cs"),
        Dinamarquês("da"),
        Holandês("nl"),
        Finlandês("fi"),
        Grego("el"),
        Turco("tr");
    }
}

sealed class Resultado<out R> {
    data class Sucesso<out T>(val dado: T?) : Resultado<T?>()
    data class Erro(val exception: Exception) : Resultado<Nothing>()
}

class TranslateRepository(private val result: ILanguage) {

    fun translate(language: LanguageData) = liveData {
        try {
            val result = result.result(language)
            if(result != ""){
                emit(Resultado.Sucesso(dado = result))
            } else {
                emit(Resultado.Erro(exception = Exception("Falha ao buscar tradução")))
            }
        } catch (e: ConnectException) {
            emit(Resultado.Erro(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(Resultado.Erro(exception = e))
        }
    }
}
