package com.business.renvest.screens.loyalty

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.business.renvest.R

/**
 * Custom ListView adapter over a shared [ArrayList] (coursework pattern).
 */
class LoyaltyRemindersListAdapter(
    private val context: Context,
    private val items: ArrayList<LoyaltyReminderRow>,
) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): LoyaltyReminderRow = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val row = convertView
            ?: LayoutInflater.from(context).inflate(R.layout.item_loyalty_reminder_row, parent, false)
        val item = items[position]
        row.findViewById<TextView>(R.id.textviewReminderTitle).text = item.title
        row.findViewById<TextView>(R.id.textviewReminderSubtitle).text = item.subtitle
        return row
    }
}
