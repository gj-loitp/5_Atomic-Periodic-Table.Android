package com.mckimquyen.atomicPeriodicTable.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mckimquyen.atomicPeriodicTable.R
import com.mckimquyen.atomicPeriodicTable.activities.tables.ElectrodeActivity
import com.mckimquyen.atomicPeriodicTable.model.Series
import java.util.Locale

class ElectrodeAdapter(
    var list: ArrayList<Series>,
    var clickListener: ElectrodeActivity,
    val context: Context
) : RecyclerView.Adapter<ElectrodeAdapter.ViewHolder>() {
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.initialize(list[position], context)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.view_electrode_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewName = itemView.findViewById(R.id.tvName) as TextView
        private val textViewShort = itemView.findViewById(R.id.tvShort) as TextView
        private val textViewCharge = itemView.findViewById(R.id.tvCharge) as TextView
        private val textViewVoltage = itemView.findViewById(R.id.tvVoltage) as TextView

        fun initialize(
            item: Series,
            context: Context
        ) {
            textViewName.text = item.name
            textViewName.text = item.name.capitalize(Locale.ROOT)
            textViewShort.text = item.short
            val voltage = item.voltage.toString()
            val shortVolt = " (Volt)"
            val textVolt = voltage + shortVolt
            textViewVoltage.text = textVolt
            textViewCharge.text = item.charge

            itemView.foreground = ContextCompat.getDrawable(context, R.drawable.shape_toast_card_ripple)
            itemView.isClickable = true
            itemView.isFocusable = true

        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: ArrayList<Series>) {
        list = filteredList
        notifyDataSetChanged()
    }

}
