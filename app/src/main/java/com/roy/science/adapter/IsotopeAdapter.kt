package com.roy.science.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.roy.science.R
import com.roy.science.model.Element
import java.util.Locale

class IsotopeAdapter(
    var elementList: ArrayList<Element>,
    var clickListener: OnElementClickListener,
    val context: Context
) : RecyclerView.Adapter<IsotopeAdapter.ViewHolder>() {
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.initialize(item = elementList[position], action = clickListener, context = context)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.isotope_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return elementList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewElement = itemView.findViewById(R.id.tv_iso_type) as TextView
        private val textViewShort = itemView.findViewById(R.id.ic_iso_type) as TextView
        private val textViewNumb = itemView.findViewById(R.id.tv_iso_numb) as TextView

        fun initialize(
            item: Element,
            action: OnElementClickListener,
            context: Context
        ) {
            textViewElement.text = item.element
            textViewElement.text = item.element.capitalize(Locale.getDefault())
            textViewShort.text = item.short
            textViewNumb.text = item.number.toString()

            itemView.foreground = ContextCompat.getDrawable(context, R.drawable.toast_card_ripple)
            itemView.isClickable = true
            itemView.isFocusable = true

            itemView.setOnClickListener {
                action.elementClickListener(item, bindingAdapterPosition)
            }
        }
    }

    interface OnElementClickListener {
        fun elementClickListener(item: Element, position: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: ArrayList<Element>) {
        elementList = filteredList
        notifyDataSetChanged()
    }
}
