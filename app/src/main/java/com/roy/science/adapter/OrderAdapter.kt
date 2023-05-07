package com.roy.science.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.roy.science.R

class OrderAdapter(dataSet: List<String> = emptyList()) : DragDropSwipeAdapter<String, OrderAdapter.ViewHolder>(dataSet) {

    class ViewHolder(itemView: View) : DragDropSwipeAdapter.ViewHolder(itemView) {
        val itemText: TextView = itemView.findViewById(R.id.tvTitleOrder)
        val dragIcon: ImageView = itemView.findViewById(R.id.ivHandleOrder)
    }

    override fun getViewHolder(itemLayout: View) = ViewHolder(itemLayout)

    override fun onBindViewHolder(
        item: String,
        viewHolder: ViewHolder,
        position: Int
    ) {
        // Here we update the contents of the view holder's views to reflect the item's data
        viewHolder.itemText.text = item
    }

    override fun getViewToTouchToStartDraggingItem(
        item: String,
        viewHolder: ViewHolder,
        position: Int
    ): View {
        // We return the view holder's view on which the user has to touch to drag the item
        return viewHolder.dragIcon
    }
}
