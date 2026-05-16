package com.business.renvest.screens.activityfeed

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.business.renvest.R

class ActivityFeedAdapter(
    private val onLongClick: (ActivityEventRowUi) -> Unit,
) : RecyclerView.Adapter<ActivityFeedAdapter.VH>() {

    private var items: List<ActivityEventRowUi> = emptyList()

    fun submitList(list: List<ActivityEventRowUi>) {
        items = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity_event_row, parent, false)
        return VH(v, onLongClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    class VH(
        itemView: android.view.View,
        private val onLongClick: (ActivityEventRowUi) -> Unit,
    ) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.textviewActivityTitle)
        private val subtitle: TextView = itemView.findViewById(R.id.textviewActivitySubtitle)

        fun bind(row: ActivityEventRowUi) {
            title.text = row.title
            subtitle.text = row.subtitle
            itemView.setOnLongClickListener {
                onLongClick(row)
                true
            }
        }
    }
}
