package com.example.tradutorlinguas.activity

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.tradutorlinguas.dataclass.LanguageData
import com.example.tradutorlinguas.Translator
import com.example.tradutorlinguas.databinding.ActivityMainBinding
import com.example.tradutorlinguas.viewmodel.ViewModelApi
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val translate = Translator()
    private val viewModelApi: ViewModelApi by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        listener()

    }

    private fun listener(){
        val list = Translator.Language.values()
        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, list)

        binding.run {
            textViewFrom.setAdapter(adapter)
            textViewTo.setAdapter(adapter)
            textViewFrom.setText(Translator.Language.Inglês.name, false)
            textViewTo.setText(Translator.Language.Português.name, false)
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
    }

    private fun observer(languageData: LanguageData) {
        translate.translate(languageData, binding.textTo)
    }

    private var TextInputLayout.text: String
        get() = editText?.text?.toString() ?: ""
        set(value) { editText?.setText(value) }

}