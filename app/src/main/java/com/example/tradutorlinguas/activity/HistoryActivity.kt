package com.example.tradutorlinguas.activity

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.tradutorlinguas.R
import com.example.tradutorlinguas.databinding.ActivityHistoryBinding
import com.example.tradutorlinguas.interfaces.IModifyValue
import com.example.tradutorlinguas.remote.PlayVoice
import com.example.tradutorlinguas.repository.Language
import com.example.tradutorlinguas.util.CaptureFlag
import com.example.tradutorlinguas.util.ReturnStateIcon
import com.example.tradutorlinguas.viewmodel.ViewModelApi
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryActivity : AppCompatActivity(), IModifyValue {

    private val binding by lazy { ActivityHistoryBinding.inflate(layoutInflater) }
    private val viewModelApi: ViewModelApi by viewModel()
    private val animatorImage: AnimatorView by inject()
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
                    playAndModifyIconPlayStop(item.textFrom.length.toLong(), imagePlayCardFrom,
                            item.textFrom, langFrom.str)
                }
                imagePlayCardTo.setOnClickListener {
                    playAndModifyIconPlayStop(item.textTo.length.toLong(), imagePlayCardTo,
                            item.textTo, langTo.str)
                }
                closeActivity.setOnClickListener {
                    playVoice.stopVoice()
                    finish()
                }
            }
        })
    }

    private fun playAndModifyIconPlayStop(interval: Long, icon: ImageView, text: String, from: String) {
        if (playOrStop == 0) {
            playOrStop = 1
            icon.setImageResource(R.drawable.ic_stop)
            playVoice.init(application, text, from)
            val returnState = ReturnStateIcon(this)
            returnState.returnStateIcon(interval, icon)

        } else {
            icon.setImageResource(R.drawable.ic_sound)
            playVoice.stopVoice()
            playOrStop = 0
        }
    }

    override fun onBackPressed() {
        playVoice.stopVoice()
        super.onBackPressed()
    }

    override fun modifyValue(value: Int) {
        playOrStop = 0
    }

}
