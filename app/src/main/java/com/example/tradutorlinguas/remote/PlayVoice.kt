package com.example.tradutorlinguas.remote

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*

class PlayVoice {

    private var tts: TextToSpeech? = null

    fun init(context: Context, str: String, lang: String){
        try { tts = TextToSpeech(context, onInitListener(str, lang)) }
        catch (e: Exception){ }
    }

    private fun onInitListener(str: String, lang: String) = TextToSpeech.OnInitListener {

        val locale = Locale(lang)
        if (it == TextToSpeech.SUCCESS){
            
            val result = tts?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("erro", "Linguagem não suportada")
            }
            else { tts?.speak(str, TextToSpeech.QUEUE_FLUSH, null) }
        }
        else{ Log.e("erro", "Não pode reproduzir") }
    }
}
