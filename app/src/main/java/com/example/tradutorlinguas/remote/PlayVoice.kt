package com.example.tradutorlinguas.remote

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*

class PlayVoice {

    private var textToSpeech: TextToSpeech? = null

    fun init(context: Context, text: String, lang: String){
        try { textToSpeech = TextToSpeech(context, onInitListener(text, lang)) }
        catch (e: Exception){ }
    }

    private fun onInitListener(str: String, lang: String) = TextToSpeech.OnInitListener {

        val locale = Locale(lang)
        if (it == TextToSpeech.SUCCESS){
            
            val result = textToSpeech?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("erro", "Linguagem não suportada")
            }
            else { textToSpeech?.speak(str, TextToSpeech.QUEUE_FLUSH, null) }
        }
        else { Log.e("erro", "Não pode reproduzir") }
    }
}
