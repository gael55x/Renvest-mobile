package com.business.renvest.screens.loyalty

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.business.renvest.R

class LoyaltyProgramsAdapter(
    private val onLongClick: (Int) -> Unit,
) : RecyclerView.Adapter<LoyaltyProgramsAdapter.VH>() {

    private var items: List<LoyaltyProgramRow> = emptyList()

    fun submitList(list: List<LoyaltyProgramRow>) {
        items = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_loyalty_program_row, parent, false)
        return VH(v, onLongClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    class VH(
        itemView: android.view.View,
        private val onLongClick: (Int) -> Unit,
    ) : RecyclerView.ViewHolder(itemView) {
        private val name = itemView.findViewById<TextView>(R.id.textviewProgramName)
        private val rule = itemView.findViewById<TextView>(R.id.textviewProgramRule)

        fun bind(row: LoyaltyProgramRow) {
            name.text = row.name
            rule.text = itemView.context.getString(
                R.string.loyalty_program_rule_format,
                row.visitsRequired,
                row.rewardDescription,
            )
            itemView.setOnLongClickListener {
                onLongClick(bindingAdapterPosition)
                true
            }
        }
    }
}
