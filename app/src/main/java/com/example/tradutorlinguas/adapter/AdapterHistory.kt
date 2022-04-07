package com.example.tradutorlinguas.adapter

import android.app.Notification
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tradutorlinguas.R
import com.example.tradutorlinguas.dataclass.LanguageData
import com.example.tradutorlinguas.interfaces.IClickItemRecycler
import com.example.tradutorlinguas.interfaces.INotification
import com.example.tradutorlinguas.util.GetColor
import org.koin.core.scope.Scope

class AdapterHistory (private val color: GetColor, private val clickItem: IClickItemRecycler,
                      private val notification: INotification):
    RecyclerView.Adapter<AdapterHistory.ViewHolderHistory>() {

    private var listHistory: ArrayList<LanguageData> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHistory {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_saves, parent, false)

        return ViewHolderHistory(item)
    }

    override fun onBindViewHolder(holder: ViewHolderHistory, position: Int) {

        val fullHistory = listHistory[position]
        holder.bindHistory(fullHistory, position)
    }

    inner class ViewHolderHistory(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val itemTextLangFrom: TextView = itemView.findViewById(R.id.tv_lang_from)
        private val itemTextLangTo: TextView = itemView.findViewById(R.id.tv_lang_to)
        private val itemTextFrom: TextView = itemView.findViewById(R.id.tv_translate_from)
        private val itemTextTo: TextView = itemView.findViewById(R.id.tv_translate_to)
        private val itemBox: ConstraintLayout = itemView.findViewById(R.id.box_employee)
        private val imageClose: ImageView = itemView.findViewById(R.id.image_close)

        init {
            itemBox.setOnClickListener(this)
            imageClose.setOnClickListener(this)
        }

        fun bindHistory(history: LanguageData, position: Int){
            itemTextLangFrom.text = history.from
            itemTextLangTo.text = history.to
            itemTextFrom.text = history.textFrom
            itemTextTo.text = history.textTo
            itemBox.setBackgroundResource(color.getColor(position))
            if (listHistory.size == position) {
                itemBox.setPadding(0, 10, 0, 0)
            }
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            when (view){
                imageClose -> clickItem.clickClose(listHistory[position].id, position)
                itemBox -> clickItem.clickBox(listHistory[position].id)
            }
        }
    }

    override fun getItemCount(): Int {
        return listHistory.count()
    }

    fun updateHistory(list: ArrayList<LanguageData>) {
        listHistory = if (list.size <= 1){ list }
        else { list.reversed() as ArrayList<LanguageData> }
        notifyDataSetChanged()
    }

    fun updateRemoveItem(position: Int){
        listHistory.removeAt(position)
        notifyItemRemoved(position)

        val size = listHistory.size
        notification.notification(size)

    }

}