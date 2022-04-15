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
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tradutorlinguas.R
import com.example.tradutorlinguas.adapter.AdapterHistory
import com.example.tradutorlinguas.databinding.ActivityMainBinding
import com.example.tradutorlinguas.dataclass.LanguageData
import com.example.tradutorlinguas.interfaces.IClickItemRecycler
import com.example.tradutorlinguas.interfaces.INotification
import com.example.tradutorlinguas.remote.PlayVoice
import com.example.tradutorlinguas.remote.Translator
import com.example.tradutorlinguas.util.CaptureFlag
import com.example.tradutorlinguas.util.CaptureHourDate
import com.example.tradutorlinguas.util.GetColor
import com.example.tradutorlinguas.viewmodel.ViewModelApi
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity(), IClickItemRecycler, INotification {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val playVoice: PlayVoice by inject()
    private lateinit var adapterHistory: AdapterHistory
    private val captureHourDate: CaptureHourDate by inject()
    private val color: GetColor by inject()
    private val capture: CaptureFlag by inject()
    private val createViewDialog: CreateDialog by inject()
    private val createViewMenu: CreateMenu by inject()
    private val viewModelApi: ViewModelApi by viewModel()
    private val permissionCode = 1000
    private var value = 0
    private var size = 0

    companion object { private const val SPEECH_REQUEST_CODE = 0 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        listener()
        recyclerHistory()
        observeHistory()
    }

    private fun recyclerHistory() {
        adapterHistory = AdapterHistory(color, this, this, capture)
        val recycler = binding.recyclerHistory
        recycler.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = adapterHistory
    }

    private fun listener(){

        val list = Translator.Language.values()
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list)

        binding.run {
            textViewFrom.setAdapter(adapter)
            textViewTo.setAdapter(adapter)
            textViewFrom.setText(Translator.Language.Português.name, false)
            textViewTo.setText(Translator.Language.Inglês.name, false)

            imgSwap.setOnClickListener {
                val from = binding.tilFrom.text
                binding.textViewFrom.setText(binding.tilTo.text, false)
                binding.textViewTo.setText(from, false)
            }

            icVoz.setOnClickListener { permissionVoice() }

            icSave.setOnClickListener {

                val textFrom = binding.textFrom.text
                val hour = captureHourDate.captureHoraCurrent()
                val date = captureHourDate.captureDateCurrent()

                if (textFrom.isEmpty() || textFrom.isBlank()){
                    toast("Não há palavra para salvar")
                }
                else{
                    val textTo = binding.textTo.text.toString()
                    val from = Translator.Language.valueOf(binding.tilFrom.text)
                    val to = Translator.Language.valueOf(binding.tilTo.text)
                    saveTranslate(LanguageData(0, from.name, to.name, textFrom, textTo, hour, date))
                }
            }
        }

        binding.icPlayTo.setOnClickListener {
            val text = binding.textTo.text.toString()
            val to = Translator.Language.valueOf(binding.tilTo.text)
            if (text.isEmpty() || text.isBlank() || text == "Tradução"){
                toast("Não há palavra para traduzir")
            }
            else{
                playVoice.init(this, text, to.str)
            }
        }

        binding.textFrom.editText?.doAfterTextChanged {

            try {
                if (it.toString().isNotBlank()){

                    val from = Translator.Language.valueOf(binding.tilFrom.text)
                    val to = Translator.Language.valueOf(binding.tilTo.text)
                    val text = binding.textFrom.text

                    if (validationInternet(this)){
                        binding.progress.visibility = View.VISIBLE
                        observer(LanguageData(0, from.str, to.str, text, "", "", ""))
                    }
                    else { binding.textTo.text = getString(R.string.verifique_internet) }
                }
                else{
                    binding.textTo.text = getString(R.string.traducao)
                    binding.icSave.setImageResource(R.drawable.ic_save_gray)
                    binding.icPlayFrom.setImageResource(R.drawable.ic_sound_gray)
                    binding.icPlayTo.setImageResource(R.drawable.ic_sound_gray)
                }
            }catch (e: Exception){
                toast("Erro na tradução, tente novamente!")
            }
        }

        binding.imageViewOption.setOnClickListener {
            val menu = createViewMenu.createMenu(this, binding.imageViewOption)
            menu.setOnMenuItemClickListener { item ->
                when (item!!.itemId) {
                    R.id.ocultar_historico -> {
                        binding.recyclerHistory.visibility = View.GONE
                        binding.textTranslateHere.visibility = View.GONE
                    }
                    R.id.excluir_tudo -> {
                        val listHistory = viewModelApi.history.value
                        if (listHistory != null) {
                            if (listHistory.size != 0) {
                                viewModelApi.removeAll(listHistory)
                                adapterHistory.updateRemoveAll(listHistory)
                            }
                            else{ toast("Não tem arquivos para excluir!") }
                        }
                    }
                    R.id.sair -> { finish() }
                }
                true
            }
            menu.show()
        }
    }

    private fun saveTranslate(languageData: LanguageData) {
        val value = viewModelApi.saveHistory(languageData)
        if (value){
            toast("Tradução Salva!")
            viewModelApi.consultHistory()
        }
        else { toast("Falha ao salvar!") }
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

        viewModelApi.translate(languageData)
        viewModelApi.translateValue.observe(this){
            it?.let { string ->
                binding.run {
                    progress.visibility = View.INVISIBLE
                    textTo.text = string
                    icSave.setImageResource(R.drawable.ic_save)
                    icPlayFrom.setImageResource(R.drawable.ic_sound)
                    icPlayTo.setImageResource(R.drawable.ic_sound)
                }
            }
        }
    }

    private fun observeHistory(){
        viewModelApi.consultHistory()
        viewModelApi.history.observe(this){
            if (it.size != 0) {
                binding.textTranslateHere.visibility = View.GONE
                adapterHistory.updateHistory(it)
                size = it.size
            }
            else {
                binding.textTranslateHere.visibility = View.VISIBLE
            }
        }
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

        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, 
                         RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            }

            if (intent.resolveActivity(packageManager) == null) {
                startActivityForResult(intent, SPEECH_REQUEST_CODE)
            }
            else { toast("Erro ao gravar") }
            
        }catch(e: Exception) {}
       
    }

    override fun clickClose(id: Int, position: Int) {
        viewModelApi.removeHistory(id)
        adapterHistory.updateRemoveItem(position)
    }

    override fun clickBox(item: LanguageData, position: Int) {

        val inflate = layoutInflater.inflate(R.layout.card_history, null)

        val imageFrom = inflate.findViewById<ImageView>(R.id.image_card_from)
        val imageTo = inflate.findViewById<ImageView>(R.id.image_card_to)
        val imagePlayFrom = inflate.findViewById<ImageView>(R.id.image_play_card_from)
        val imagePlayTo = inflate.findViewById<ImageView>(R.id.image_play_card_to)
        val textLangFrom = inflate.findViewById<TextView>(R.id.tv_lang_from)
        val textLangTo = inflate.findViewById<TextView>(R.id.tv_lang_to)
        val textFrom = inflate.findViewById<TextView>(R.id.tv_translate_from)
        val textTo = inflate.findViewById<TextView>(R.id.tv_translate_to)

        imageFrom.setImageResource(capture.capture(item.from))
        imageTo.setImageResource(capture.capture(item.to))
        textFrom.text = item.textFrom
        textTo.text = item.textTo
        textLangFrom.text = item.from
        textLangTo.text = item.to

        val langFrom = Translator.Language.valueOf(item.from)
        val langTo = Translator.Language.valueOf(item.to)

        imagePlayFrom.setOnClickListener {
            playVoice.init(this, item.textFrom, langFrom.str)
        }
        imagePlayTo.setOnClickListener {
            playVoice.init(this, item.textTo, langTo.str)
        }

        val alertDialog = createViewDialog.createDialog(
                "Salvo às ${item.hour}, dia ${item.date}", this)
        alertDialog.setPositiveButton("Fechar"){ _,_ -> }
        alertDialog.setNegativeButton("Exluir"){ _,_ ->
            viewModelApi.removeHistory(item.id)
            adapterHistory.updateRemoveItem(position)
        }
        alertDialog.setView(inflate)
        alertDialog.create().show()
    }

    override fun notification(value: Int) {
        if (value != 0) { binding.textTranslateHere.visibility = View.INVISIBLE }
        else{ binding.textTranslateHere.visibility = View.VISIBLE }
    }

    private fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
