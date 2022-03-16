package com.example.tradutorlinguas

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import com.example.tradutorlinguas.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val translate = Translator()

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
            if (it != null && it.toString().isNotEmpty()){
                val from = binding.tilFrom.text
                val fr = Translator.Language.valueOf(from)
                val to = binding.tilTo.text
                val t = Translator.Language.valueOf(to)
                val text = binding.textFrom.text

                val value = translate.translate(fr.str, t.str, text)
                binding.textTo.text = value
            }
        }
    }

    private var TextInputLayout.text: String
        get() = editText?.text?.toString() ?: ""
        set(value) { editText?.setText(value) }

}