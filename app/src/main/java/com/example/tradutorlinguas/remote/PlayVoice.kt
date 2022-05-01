package com.example.tradutorlinguas.remote

import android.app.Application
import android.speech.tts.TextToSpeech
import android.util.Log
import com.example.tradutorlinguas.activity.MainActivity
import java.util.*

class PlayVoice {

    private var textToSpeech: TextToSpeech? = null

    fun init(context: Application, text: String, lang: String, playOrStop: Int){
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
            val result = textToSpeech?.setLanguage(Locale(lang))

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("erro", "Linguagem não suportada")
            }
            else { textToSpeech?.speak(str, TextToSpeech.QUEUE_FLUSH, null) }
        }
        else { Log.e("erro", "Não pode reproduzir") }
    }

    fun stopVoice(){ textToSpeech?.shutdown() }
}
