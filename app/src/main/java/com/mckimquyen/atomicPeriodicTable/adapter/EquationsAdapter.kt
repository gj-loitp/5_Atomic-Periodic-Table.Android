package com.mckimquyen.atomicPeriodicTable.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mckimquyen.atomicPeriodicTable.R
import com.mckimquyen.atomicPeriodicTable.activities.tables.EquationsActivity
import com.mckimquyen.atomicPeriodicTable.model.Equation
import com.mckimquyen.atomicPeriodicTable.pref.ThemePref

class EquationsAdapter(
    var list: ArrayList<Equation>,
    var clickListener: EquationsActivity,
    val context: Context
) : RecyclerView.Adapter<EquationsAdapter.ViewHolder>() {
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.initialize(item = list[position], action = clickListener, context = context)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.view_row_equations_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val equTitle = itemView.findViewById(R.id.tvEqu) as TextView
        private val equCategory = itemView.findViewById(R.id.tvIcEqu) as TextView
        private val equImg = itemView.findViewById(R.id.ivIcEqView) as ImageView

        fun initialize(item: Equation, action: OnEquationClickListener, context: Context) {
            equTitle.text = item.equationTitle
            if (item.category == "Mechanics") {
                equCategory.text = "Me"
            }
            if (item.category == "General") {
                equCategory.text = "Ge"
            }
            if (item.category == "Theory of Relativity") {
                equCategory.text = "TR"
            }
            if (item.category == "Thermodynamics") {
                equCategory.text = "Th"
            }
            if (item.category == "Wavelengths") {
                equCategory.text = "Wv"
            }
            if (item.category == "Electricity") {
                equCategory.text = "El"
            }
            if (item.category == "Magnetism and Induction") {
                equCategory.text = "MI"
            }
            if (item.category == "Atomic Physics") {
                equCategory.text = "AP"
            }
            if (item.category == "Nuclear Physics") {
                equCategory.text = "NP"
            }
            equImg.setImageResource(item.equation)
            val themePref = ThemePref(context)
            val themePrefValue = themePref.getValue()
            if (themePrefValue == 1) {
                equImg.colorFilter = ColorMatrixColorFilter(NEGATIVE)
            }
            if (themePrefValue == 100) {
                when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        equImg.colorFilter = ColorMatrixColorFilter(NEGATIVE)
                    }
                }
            }
            itemView.foreground = ContextCompat.getDrawable(context, R.drawable.shape_toast_card_ripple)
            itemView.isClickable = true
            itemView.isFocusable = true
            itemView.setOnClickListener {
                action.equationClickListener(item, adapterPosition)
            }
        }

        private val NEGATIVE = floatArrayOf(
            -1.0f, 0f, 0f, 0f, 255f,
            0f, -1.0f, 0f, 0f, 255f,
            0f, 0f, -1.0f, 0f, 255f,
            0f, 0f, 0f, 1.0f, 0f
        )
    }


    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: ArrayList<Equation>) {
        list = filteredList
        notifyDataSetChanged()
    }

    interface OnEquationClickListener {
        fun equationClickListener(item: Equation, position: Int)
    }

}
