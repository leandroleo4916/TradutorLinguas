package com.example.tradutorlinguas.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tradutorlinguas.databinding.ActivityHistoryBinding
import com.example.tradutorlinguas.remote.PlayVoice
import com.example.tradutorlinguas.remote.Translator
import com.example.tradutorlinguas.util.CaptureFlag
import com.example.tradutorlinguas.viewmodel.ViewModelApi
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHistoryBinding.inflate(layoutInflater) }
    private val viewModelApi: ViewModelApi by viewModel()
    private val playVoice: PlayVoice by inject()
    private val capture: CaptureFlag by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bundle = intent.extras
        val id = bundle?.getString("id")
        openInfo(id)
    }

    private fun openInfo(id: String?) {

        id?.let { viewModelApi.consultHistoryById(it) }
        viewModelApi.historyById.observe(this, { item ->
            binding.run {

                imageCardFrom.setImageResource(capture.capture(item.from))
                imageCardTo.setImageResource(capture.capture(item.to))

                tvLangFrom.text = item.from
                tvTranslateFrom.text = item.textFrom
                tvLangTo.text = item.to
                tvTranslateTo.text = item.textTo

                val langFrom = Translator.Language.valueOf(item.from)
                val langTo = Translator.Language.valueOf(item.to)

                imagePlayCardFrom.setOnClickListener {
                    playVoice.init(application, item.textFrom, langFrom.str)
                }
                imagePlayCardTo.setOnClickListener {
                    playVoice.init(application, item.textTo, langTo.str)
                }
            }
        })

    }

}
