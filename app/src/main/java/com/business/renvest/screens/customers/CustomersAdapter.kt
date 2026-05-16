package com.business.renvest.screens.customers

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.business.renvest.R

class CustomersAdapter : RecyclerView.Adapter<CustomersAdapter.VH>() {

    private var items: List<CustomerRowUi> = emptyList()

    fun submitList(list: List<CustomerRowUi>) {
        items = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_customer_list_row, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    class VH(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.textviewCustomerName)

        fun bind(row: CustomerRowUi) {
            name.text = row.displayName
        }
    }
}
