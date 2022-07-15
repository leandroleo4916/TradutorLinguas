package com.example.tradutorlinguas.activity

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tradutorlinguas.R
import com.example.tradutorlinguas.adapter.AdapterHistory
import com.example.tradutorlinguas.databinding.ActivityMainBinding
import com.example.tradutorlinguas.dataclass.LanguageData
import com.example.tradutorlinguas.interfaces.IClickItemRecycler
import com.example.tradutorlinguas.interfaces.IModifyValue
import com.example.tradutorlinguas.interfaces.INotification
import com.example.tradutorlinguas.remote.PlayVoice
import com.example.tradutorlinguas.repository.Language
import com.example.tradutorlinguas.util.*
import com.example.tradutorlinguas.viewmodel.ViewModelApi
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), IClickItemRecycler, INotification, IModifyValue {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val playVoice: PlayVoice by inject()
    private val animatorImage: AnimatorView by inject()
    private val modifyIcon: ModifyIcon by inject()
    private val showToast: ShowToast by inject()
    private val formatText: FormatText by inject()
    private lateinit var adapterHistory: AdapterHistory
    private val captureHourDate: CaptureHourDate by inject()
    private val securityPreferences: SecurityPreferences by inject()
    private val capture: CaptureFlag by inject()
    private val createViewMenu: CreateMenu by inject()
    private val viewModelApi: ViewModelApi by viewModel()
    private val permissionCode = 1000
    private var value = 0
    private var size = 0
    private var valueView = 0
    private var getValue = ""
    private var playOrStop = 0
    private var sizeLayout by Delegates.notNull<Int>()
    private var sizeHistory = 0

    companion object { private const val SPEECH_REQUEST_CODE = 0 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getValue = securityPreferences.getStoredString("value").toString()

        hideRecycler()
        showMenu()
        listener()
        recyclerHistory()
        observeHistory()
        hideRecyclerWhenKeyBoard()
    }

    private fun hideRecyclerWhenKeyBoard(){
        val box = binding.constraintTranslate
        sizeLayout = box.height
        binding.constraintTranslate.viewTreeObserver.addOnGlobalLayoutListener{

            getValue = securityPreferences.getStoredString("value").toString()
            if (getValue == "enable"){
                val size = box.height
                if (sizeLayout == 0) sizeLayout = size
                if (sizeLayout > size) hideRecyclerView()
                else showRecycler()
            }
        }
    }

    private fun showRecycler(){
        binding.recyclerHistory.visibility = View.VISIBLE
        if (sizeHistory == 0) {
            binding.textTranslateHere.visibility = View.VISIBLE
        }
    }
    
    private fun hideRecycler() {
        if (getValue == "disable") hideRecyclerView()
    }

    private fun hideRecyclerView(){
        binding.textTranslateHere.visibility = View.GONE
        binding.recyclerHistory.visibility = View.GONE
    }

    private fun recyclerHistory() {
        adapterHistory = AdapterHistory(this, this, capture, animatorImage)
        val recycler = binding.recyclerHistory
        recycler.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = adapterHistory
    }

    private fun listener() {

        val list = Language.values()
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list)

        binding.run {

            textViewFrom.setAdapter(adapter)
            textViewTo.setAdapter(adapter)
            textViewFrom.setText(Language.Português.name, false)
            textViewTo.setText(Language.Inglês.name, false)

            imgSwap.setOnClickListener {
                animatorImage.animationView(imgSwap)
                val from = tilFrom.text
                textViewFrom.setText(tilTo.text, false)
                textViewTo.setText(from, false)
            }

            icVoz.setOnClickListener {
                animatorImage.animationView(icVoz)
                permissionVoice()
            }

            icSave.setOnClickListener {

                animatorImage.animationView(icSave)
                val textFrom = textFrom.text
                val textTo = textTo.text
                val hour = captureHourDate.captureHoraCurrent()
                val date = captureHourDate.captureDateCurrent()

                if (textFrom.isNotEmpty() && textTo != "") {
                    val from = Language.valueOf(tilFrom.text)
                    val to = Language.valueOf(tilTo.text)
                    saveTranslate(LanguageData(0, from.name, to.name, textFrom, textTo.toString(), hour, date))
                }
            }

            textFrom.editText?.doAfterTextChanged {
                if (it.toString().isNotBlank()) {
                    if (it.toString().length == 1) modifyIcon.iconsShow(binding)
                } else {
                    modifyIcon.iconsHide(binding)
                    textTo.text = ""
                }
            }

            icSendFrom.setOnClickListener {
                animatorImage.animationView(icSendFrom)
                try {
                    if (textFrom.text.isNotBlank()) {
                        progress.visibility = View.VISIBLE
                        textTranslate.text = getString(R.string.traduzindo)
                        val from = Language.valueOf(tilFrom.text)
                        val to = Language.valueOf(tilTo.text)
                        val text = textFrom.text
                        translate(from.str, to.str, text)
                    }
                } catch (e: Exception) {
                    showToast.toast("Erro na tradução, tente novamente!", application)
                }
            }

            icCopyFrom.setOnClickListener {
                animatorImage.animationView(icCopyFrom)

                val from = Language.valueOf(tilFrom.text)
                val to = Language.valueOf(tilTo.text)
                val textFrom = textFrom.text
                val textTo = textTo.text
                val textComplete = "$from\n$textFrom\n\n$to\n$textTo"

                if (textFrom.isNotEmpty() && textTo != "") {
                    val copy = ClipData.newPlainText("text", textComplete)
                    val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    clipboardManager.setPrimaryClip(copy)
                    showToast.toast("Copiado!", application)
                }
            }

            icPlayTo.setOnClickListener {
                animatorImage.animationView(icPlayTo)
                val text = textTo.text.toString()
                val to = Language.valueOf(tilTo.text)

                if (text.isNotEmpty() || text.isNotBlank()) {
                    playAndModifyIconPlayStop(text.length.toLong(), icPlayTo, text, to.str)
                }
            }

            icPlayFrom.setOnClickListener {
                animatorImage.animationView(icPlayFrom)
                val text = textFrom.text
                val from = Language.valueOf(tilFrom.text)

                if (text.isNotEmpty() || text.isNotBlank()) {
                    playAndModifyIconPlayStop(text.length.toLong(), icPlayFrom, text, from.str)
                }
            }

            icShare.setOnClickListener {
                animatorImage.animationView(icShare)

                val from = Language.valueOf(tilFrom.text)
                val to = Language.valueOf(tilTo.text)
                val textFrom = textFrom.text
                val textTo = textTo.text
                val textComplete = "$from\n$textFrom\n\n$to\n$textTo"

                if (textFrom.isNotEmpty() && textTo != "") intentShare(textComplete)
            }

            icSwapUp.setOnClickListener {
                animatorImage.animationView(icSwapUp)
                val from = textFrom.text
                val to = textTo.text
                if(from != "" && to != "") {
                    textFrom.text = to.toString()
                    textTo.text = from
                }
            }
        }
    }

    private fun intentShare(text: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/translate"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(sharingIntent, "Tradução"))
    }

    private fun translate(from: String, to: String, text: String) {
        if (validationInternet(this)) {
            observer(LanguageData(0, from, to, text, "", "", ""))
        }
        else binding.textTo.text = getString(R.string.verifique_internet)
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

    private fun showMenu() {

        binding.imageViewOption.setOnClickListener {

            getValue = securityPreferences.getStoredString("value").toString()
            if (getValue == "enable" || getValue == "") {
                val menu = createViewMenu.createMenu(this, binding.imageViewOption, R.menu.option)
                menu.setOnMenuItemClickListener { item ->
                    when (item!!.itemId) {
                        R.id.ocultar_historico -> {
                            binding.recyclerHistory.visibility = View.GONE
                            binding.textTranslateHere.visibility = View.GONE
                            securityPreferences.removeEnable()
                            securityPreferences.putStoreString("value", "disable")
                            valueView = 1
                        }
                        R.id.excluir_tudo -> removeAllHistory()
                        R.id.sair -> finish()
                    }
                    true
                }
                menu.show()
            } else {
                val menu = createViewMenu.createMenu(this, binding.imageViewOption, R.menu.option_v2)
                menu.setOnMenuItemClickListener { item ->
                    when (item!!.itemId) {
                        R.id.mostrar_historico -> {
                            val size = viewModelApi.consultSizeList()
                            if (size != 0) {
                                binding.textTranslateHere.visibility = View.GONE
                            } else {
                                binding.textTranslateHere.visibility = View.VISIBLE
                            }
                            binding.recyclerHistory.visibility = View.VISIBLE
                            securityPreferences.removeDisable()
                            securityPreferences.putStoreString("value", "enable")
                            valueView = 0
                        }
                        R.id.excluir_tudo -> removeAllHistory()
                        R.id.sair -> finish()
                    }
                    true
                }
                menu.show()
            }
        }
    }

    private fun removeAllHistory() {
        val listHistory = viewModelApi.history.value
        if (listHistory != null) {
            if (listHistory.size != 0) {
                viewModelApi.removeAll(listHistory)
                adapterHistory.updateRemoveAll(listHistory)
            } else {
                showToast.toast("Não tem arquivos para excluir!", this@MainActivity)
            }
        }
    }

    private fun saveTranslate(languageData: LanguageData) {
        val value = viewModelApi.saveHistory(languageData)
        if (value) {
            showToast.toast("Tradução Salva!", this@MainActivity)
            viewModelApi.consultHistory()
        } else showToast.toast("Falha ao salvar!", this@MainActivity)
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
            modifyIcon.iconsShow(binding)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun observer(languageData: LanguageData) {

        viewModelApi.translate(languageData)
        viewModelApi.translateValue.observe(this) {

            try {
                it?.let { string ->
                    val text = formatText.formatText(string)

                    binding.progress.visibility = View.INVISIBLE
                    binding.textTranslate.text = getString(R.string.traducao)
                    binding.textTo.text = text
                    modifyIcon.iconSaveShow(binding)
                }
            } catch (e: Exception) { }
        }
    }

    private fun observeHistory() {
        viewModelApi.consultHistory()
        viewModelApi.history.observe(this) {
            if (it.size != 0) {
                binding.textTranslateHere.visibility = View.GONE
                adapterHistory.updateHistory(it)
                size = it.size
                sizeHistory = it.size
            } else {
                if (getValue == "disable") binding.textTranslateHere.visibility = View.GONE
                else binding.textTranslateHere.visibility = View.VISIBLE
            }
        }
    }

    private var TextInputLayout.text: String
        get() = editText?.text?.toString() ?: ""
        set(value) { editText?.setText(value) }

    private fun permissionVoice() {

        if (this.let {
                    ContextCompat.checkSelfPermission(it, Manifest.permission.RECORD_AUDIO)
                }
                == PackageManager.PERMISSION_DENIED ||

                this.let {
                    ContextCompat.checkSelfPermission(it, Manifest.permission.RECORD_AUDIO)
                }
                == PackageManager.GET_PERMISSIONS) {

            val permission = arrayOf(Manifest.permission.RECORD_AUDIO)
            requestPermissions(permission, permissionCode)

        } else openVoice()
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
                    showToast.toast("Permissão negada", this@MainActivity)
                }
            }
        }
    }

    private fun openVoice() {

        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            }

            if (intent.resolveActivity(packageManager) == null) {
                startActivityForResult(intent, SPEECH_REQUEST_CODE)
            } else {
                showToast.toast("Erro ao gravar", this@MainActivity)
            }

        } catch (e: Exception) { }

    }

    override fun clickClose(id: Int, position: Int) {
        viewModelApi.removeHistory(id)
        adapterHistory.updateRemoveItem(position)
    }

    override fun clickBox(id: Int) {
        val bundle = Bundle()
        val intent = Intent(this, HistoryActivity::class.java)
        bundle.putString("id", id.toString())
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun notification(value: Int) {
        if (value != 0) {
            binding.textTranslateHere.visibility = View.GONE
        } else {
            getValue = securityPreferences.getStoredString("value").toString()
            sizeHistory = 0
            if (getValue == "disable") binding.textTranslateHere.visibility = View.GONE
            else binding.textTranslateHere.visibility = View.VISIBLE
        }
    }

    override fun modifyValue(value: Int) { playOrStop = 0 }

}
