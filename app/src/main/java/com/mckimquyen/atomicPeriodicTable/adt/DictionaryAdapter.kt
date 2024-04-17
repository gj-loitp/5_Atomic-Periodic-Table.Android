package com.mckimquyen.atomicPeriodicTable.adt

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mckimquyen.atomicPeriodicTable.R
import com.mckimquyen.atomicPeriodicTable.activities.tables.DictionaryActivity
import com.mckimquyen.atomicPeriodicTable.model.Dictionary

class DictionaryAdapter(
    var dictionaryList: ArrayList<Dictionary>,
    var clickListener: DictionaryActivity,
    val con: Context
) : RecyclerView.Adapter<DictionaryAdapter.ViewHolder>() {
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        holder.initialize(item = dictionaryList[position], action = clickListener, con = con)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.view_text_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return dictionaryList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val heading = itemView.findViewById(R.id.tvTitle) as TextView
        private val text = itemView.findViewById(R.id.tvText) as TextView
        private val wikiBtn = itemView.findViewById(R.id.tvWikiBtn) as TextView
        private val frame = itemView.findViewById(R.id.rCard) as FrameLayout

        fun initialize(
            item: Dictionary,
            action: OnDictionaryClickListener,
            con: Context
        ) {

            heading.text = item.heading
            text.text = item.text
            val url = item.wiki

            wikiBtn.foreground = ContextCompat.getDrawable(con, R.drawable.shape_toast_card_ripple)
            wikiBtn.isClickable = true
            wikiBtn.isFocusable = true

            wikiBtn.setOnClickListener {
                action.dictionaryClickListener(item, wikiBtn, url, adapterPosition)
            }
        }
    }

    interface OnDictionaryClickListener {
        fun dictionaryClickListener(item: Dictionary, wiki: TextView, url: String, position: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: ArrayList<Dictionary>) {
        dictionaryList = filteredList
        notifyDataSetChanged()
    }

}
