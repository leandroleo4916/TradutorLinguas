package com.example.tradutorlinguas.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.example.tradutorlinguas.R
import com.example.tradutorlinguas.databinding.ActivityMainBinding
import com.example.tradutorlinguas.dataclass.LanguageData
import com.example.tradutorlinguas.remote.PlayVoice
import com.example.tradutorlinguas.remote.Translator
import com.example.tradutorlinguas.viewmodel.ViewModelApi
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val translate = Translator()
    private val playVoice = PlayVoice()
    private val permissionCode = 1000
    private val viewModelApi: ViewModelApi by viewModel()

    companion object { private const val SPEECH_REQUEST_CODE = 0 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        listener()

    }

    private fun listener(){
        val list = Translator.Language.values()
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list)

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

            try {
                if (it.toString().isNotBlank()){

                    val from = Translator.Language.valueOf(binding.tilFrom.text)
                    val to = Translator.Language.valueOf(binding.tilTo.text)
                    val text = binding.textFrom.text

                    if (validationInternet(this)){
                        observer(LanguageData(from.str, to.str, text))
                    }
                    else { binding.textTo.text = "Verifique sua conexão com a internet!" }
                }
                else{
                    binding.textTo.text = ""
                    binding.icSave.setImageResource(R.drawable.ic_save_gray)
                    binding.icPlayFrom.setImageResource(R.drawable.ic_sound_gray)
                    binding.icPlayTo.setImageResource(R.drawable.ic_sound_gray)
                }
            }catch (e: Exception){}

        }

        binding.icVoz.setOnClickListener { permissionVoice() }

        binding.icPlayFrom.setOnClickListener {
            val text = binding.textFrom.text
            val from = Translator.Language.valueOf(binding.tilFrom.text)
            if (text.isEmpty() || text.isBlank()){
                toast("Digite uma palavra")
            }
            else{
                playVoice.init(this, text, from.str)
            }
        }

        binding.icPlayTo.setOnClickListener {
            val text = binding.textTo.text.toString()
            val to = Translator.Language.valueOf(binding.tilTo.text)
            if (text.isEmpty() || text.isBlank()){
                toast("Não há palavra para traduzir")
            }
            else{
                playVoice.init(this, text, to.str)
            }
        }
    }

    private fun validationInternet(context: Context): Boolean {

        val result: Boolean
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivity.activeNetwork ?: return false
        val getNetwork = connectivity.getNetworkCapabilities(network) ?: return false

        result = when {
            getNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            getNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
        return result
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
        translate.translate(languageData, binding.textTo, binding.icSave,
                binding.icPlayFrom, binding.icPlayTo)
    }

    private var TextInputLayout.text: String
        get() = editText?.text?.toString() ?: ""
        set(value) { editText?.setText(value) }

    private fun permissionVoice() {

        if (this.let {
                ContextCompat.checkSelfPermission(it, Manifest.permission.RECORD_AUDIO) }
                == PackageManager.PERMISSION_DENIED ||

                this.let {
                ContextCompat.checkSelfPermission(it, Manifest.permission.RECORD_AUDIO) }
                == PackageManager.GET_PERMISSIONS) {

            val permission = arrayOf(Manifest.permission.RECORD_AUDIO)
            requestPermissions(permission, permissionCode)

        } else { openVoice() }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    openVoice()
                } else {
                    toast("Permissão negada")
                }
            }
        }
    }

    private fun openVoice(){

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }

        if (intent.resolveActivity(packageManager) == null) {
            startActivityForResult(intent, SPEECH_REQUEST_CODE)
        }
        else { Toast.makeText(this, "Erro ao gravar", Toast.LENGTH_SHORT).show() }
    }

    private fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}