package com.example.tradutorlinguas.activity

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.tradutorlinguas.dataclass.LanguageData
import com.example.tradutorlinguas.remote.Translator
import com.example.tradutorlinguas.databinding.ActivityMainBinding
import com.example.tradutorlinguas.viewmodel.ViewModelApi
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val translate = Translator()
    private val viewModelApi: ViewModelApi by viewModel()

    companion object {
        private const val SPEECH_REQUEST_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        listener()

    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun listener(){
        val list = Translator.Language.values()
        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, list)

        binding.run {
            textViewFrom.setAdapter(adapter)
            textViewTo.setAdapter(adapter)
            textViewFrom.setText(Translator.Language.Português.name, false)
            textViewTo.setText(Translator.Language.Inglês.name, false)
        }

        binding.imgSwap.setOnClickListener {
            val from = binding.tilFrom.text
            binding.textViewFrom.setText(binding.tilTo.text, false)
            binding.textViewTo.setText(from, false)
        }

        binding.textFrom.editText?.doAfterTextChanged {

            val text = binding.textFrom.text
            if (it != null && it.toString().isNotEmpty()){
                val from = binding.tilFrom.text
                val fr = Translator.Language.valueOf(from)
                val to = binding.tilTo.text
                val t = Translator.Language.valueOf(to)

                observer(LanguageData(fr.str, t.str, text))
            }
            else{
                binding.textTo.text = ""
            }
        }

        binding.icVoz.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            }

            if (intent.resolveActivity(packageManager) == null) {
                startActivityForResult(intent, SPEECH_REQUEST_CODE)
            }
            else {
                Toast.makeText(this, "Erro ao gravar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                    data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let { results ->
                        results?.get(0)
                    }
            binding.textFrom.text = spokenText.toString()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun observer(languageData: LanguageData) {
        translate.translate(languageData, binding.textTo)
    }

    private var TextInputLayout.text: String
        get() = editText?.text?.toString() ?: ""
        set(value) { editText?.setText(value) }

}