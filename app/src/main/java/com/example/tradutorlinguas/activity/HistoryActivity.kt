package com.example.tradutorlinguas.activity

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.tradutorlinguas.R
import com.example.tradutorlinguas.databinding.ActivityHistoryBinding
import com.example.tradutorlinguas.remote.PlayVoice
import com.example.tradutorlinguas.repository.Language
import com.example.tradutorlinguas.util.CaptureFlag
import com.example.tradutorlinguas.viewmodel.ViewModelApi
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHistoryBinding.inflate(layoutInflater) }
    private val viewModelApi: ViewModelApi by viewModel()
    private val playVoice: PlayVoice by inject()
    private val capture: CaptureFlag by inject()
    private var playOrStop = 0

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

                textToolbar.text = "Salvo dia ${item.date} às ${item.hour}"
                tvLangFrom.text = item.from
                tvTranslateFrom.text = item.textFrom
                tvLangTo.text = item.to
                tvTranslateTo.text = item.textTo

                val langFrom = Language.valueOf(item.from)
                val langTo = Language.valueOf(item.to)

                imagePlayCardFrom.setOnClickListener {
                    playVoice.init(application, item.textFrom, langFrom.str, playOrStop)
                    setValuePlayOrStop(binding.imagePlayCardFrom)
                    imageCardTo.setImageResource(R.drawable.ic_sound_gray)
                }
                imagePlayCardTo.setOnClickListener {
                    playVoice.init(application, item.textTo, langTo.str, playOrStop)
                    setValuePlayOrStop(binding.imagePlayCardTo)
                    imageCardFrom.setImageResource(R.drawable.ic_sound_gray)
                }
            }
        })

        binding.closeActivity.setOnClickListener {
            playVoice.stopVoice()
            finish()
        }
    }

    override fun onBackPressed() {
        playVoice.stopVoice()
        super.onBackPressed()
    }

    private fun setValuePlayOrStop(icPlay: ImageView) {
        playOrStop = if (playOrStop == 0) {
            modifyIconPlayStop(icPlay)
            1
        } else {
            modifyIconPlayStop(icPlay)
            0
        }
    }

    private fun modifyIconPlayStop(icon: ImageView){
        if (playOrStop == 0) { icon.setImageResource(R.drawable.ic_stop_write) }
        else { icon.setImageResource(R.drawable.ic_sound_gray) }
    }

}
