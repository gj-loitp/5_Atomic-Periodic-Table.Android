package com.mckimquyen.atomicPeriodicTable.adapter

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
import com.mckimquyen.atomicPeriodicTable.activities.tables.IonActivity
import com.mckimquyen.atomicPeriodicTable.model.Ion
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.util.Locale

class IonAdapter(
    var list: ArrayList<Ion>,
    var clickListener: IonActivity,
    val context: Context
) :
    RecyclerView.Adapter<IonAdapter.ViewHolder>() {
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
        val v = LayoutInflater.from(parent.context).inflate(R.layout.view_row_ions_list, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView = itemView.findViewById(R.id.ionCard) as FrameLayout
        private val textViewName = itemView.findViewById(R.id.tvNameD) as TextView
        private val textViewShort = itemView.findViewById(R.id.tvShortD) as TextView
        private val textViewCharge = itemView.findViewById(R.id.tvEnd) as TextView
        private val textViewVoltage = itemView.findViewById(R.id.tvIonization) as TextView

        @SuppressLint("SetTextI18n")
        fun initialize(
            item: Ion,
            action: OnIonClickListener,
            context: Context
        ) {
            val jsonString: String?
            try {
                val ext = ".json"
                val element = item.name
                val elementJson = "$element$ext"

                val inputStream: InputStream = context.assets.open(elementJson)
                jsonString = inputStream.bufferedReader().use { it.readText() }

                val jsonArray = JSONArray(jsonString)
                val jsonObject: JSONObject = jsonArray.getJSONObject(0)

                val ionization1 = jsonObject.optString("element_ionization_energy1", "---")
                textViewVoltage.text = ionization1


            } catch (e: IOException) {
                e.printStackTrace()
            }
            textViewName.text = item.name
            textViewName.text = item.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
            textViewShort.text = item.short
            textViewCharge.text = "View all" + " " + item.count.toString()

            cardView.foreground = ContextCompat.getDrawable(context, R.drawable.shape_toast_card_ripple)
            cardView.isClickable = true
            cardView.isFocusable = true
            cardView.setOnClickListener {
                action.ionClickListener(item, bindingAdapterPosition)
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: ArrayList<Ion>) {
        list = filteredList
        notifyDataSetChanged()

    }

    interface OnIonClickListener {
        fun ionClickListener(item: Ion, position: Int)
    }
}
