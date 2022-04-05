package com.example.tradutorlinguas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tradutorlinguas.R
import com.example.tradutorlinguas.dataclass.LanguageData

class AdapterHistory: RecyclerView.Adapter<AdapterHistory.ViewHolderHistory>() {

    private var listHistory: ArrayList<LanguageData> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHistory {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_saves, parent, false)

        return ViewHolderHistory(item)
    }

    override fun onBindViewHolder(holder: ViewHolderHistory, position: Int) {

        val fullHistory = listHistory[position]
        holder.bindHistory(fullHistory)
    }

    inner class ViewHolderHistory(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val itemTextLangFrom: TextView = itemView.findViewById(R.id.tv_recycler_lang_from)
        private val itemTextLangTo: TextView = itemView.findViewById(R.id.tv_recycler_lang_to)
        private val itemTextTo: TextView = itemView.findViewById(R.id.tv_translate_from)
        private val itemTextFrom: TextView = itemView.findViewById(R.id.tv_translate_to)

        fun bindHistory(history: LanguageData){
            itemTextLangFrom.text = history.from
            itemTextLangTo.text = history.to
            itemTextFrom.text = history.textFrom
            itemTextTo.text = history.textTo
        }

        override fun onClick(view: View?) {

        }
    }

    override fun getItemCount(): Int {
        return listHistory.count()
    }

    fun updateHistory(list: ArrayList<LanguageData>) {
        listHistory = list
        notifyDataSetChanged()
    }

}