package com.example.tradutorlinguas.remote

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*

class PlayVoice {

    private var textToSpeech: TextToSpeech? = null

    fun init(context: Context, text: String, lang: String, playOrStop: Int){
        try {
            if (playOrStop == 0) {
                textToSpeech = TextToSpeech(context, onInitListener(text, lang))
            }
            else { stopVoice() }
        }
        catch (e: Exception) { }
    }

    private fun onInitListener(str: String, lang: String) = TextToSpeech.OnInitListener {

        if (it == TextToSpeech.SUCCESS) {
            val locale = Locale(lang)
            val result = textToSpeech?.setLanguage(locale)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("erro", "Linguagem não suportada")
            }
            else { textToSpeech?.speak(str, TextToSpeech.QUEUE_FLUSH, null) }
        }
        else { Log.e("erro", "Não pode reproduzir") }
    }

    fun stopVoice(){ textToSpeech?.shutdown() }
}
