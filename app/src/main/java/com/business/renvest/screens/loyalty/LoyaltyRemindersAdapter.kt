package com.business.renvest.screens.loyalty

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.business.renvest.R

class LoyaltyRemindersAdapter(
    private val onClick: (Int) -> Unit,
    private val onLongClick: (Int) -> Unit,
) : RecyclerView.Adapter<LoyaltyRemindersAdapter.VH>() {

    private var items: List<LoyaltyReminderRow> = emptyList()

    fun submitList(list: List<LoyaltyReminderRow>) {
        items = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_loyalty_reminder_row, parent, false)
        return VH(v, onClick, onLongClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    class VH(
        itemView: android.view.View,
        private val onClick: (Int) -> Unit,
        private val onLongClick: (Int) -> Unit,
    ) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.textviewReminderTitle)
        private val subtitle = itemView.findViewById<TextView>(R.id.textviewReminderSubtitle)

        fun bind(row: LoyaltyReminderRow) {
            title.text = row.title
            subtitle.text = row.subtitle
            itemView.setOnClickListener { onClick(bindingAdapterPosition) }
            itemView.setOnLongClickListener {
                onLongClick(bindingAdapterPosition)
                true
            }
        }
    }
}
