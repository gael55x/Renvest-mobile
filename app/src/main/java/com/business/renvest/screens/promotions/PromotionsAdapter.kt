package com.business.renvest.screens.promotions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.business.renvest.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class PromotionsAdapter(
    private val onItemClick: (PromotionItem) -> Unit,
    private val onSecondaryStub: () -> Unit,
) : RecyclerView.Adapter<PromotionsAdapter.PromotionViewHolder>() {

    private var items: List<PromotionItem> = emptyList()

    fun submitList(list: List<PromotionItem>) {
        items = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromotionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_promotion_row, parent, false)
        return PromotionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PromotionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class PromotionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val card: MaterialCardView = itemView.findViewById(R.id.materialcardPromotionRow)
        private val iconSlot: View = itemView.findViewById(R.id.framelayoutPromoIcon)
        private val badge: TextView = itemView.findViewById(R.id.textviewPromoBadge)
        private val title: TextView = itemView.findViewById(R.id.textviewPromoTitle)
        private val reward: TextView = itemView.findViewById(R.id.textviewPromoReward)
        private val expiry: TextView = itemView.findViewById(R.id.textviewPromoExpiry)
        private val enrolled: TextView = itemView.findViewById(R.id.textviewPromoEnrolled)
        private val usage: TextView = itemView.findViewById(R.id.textviewPromoUsage)
        private val progress: ProgressBar = itemView.findViewById(R.id.progressPromo)

        fun bind(item: PromotionItem) {
            title.text = item.title
            reward.text = item.reward
            expiry.text = item.expiry
            enrolled.text = item.enrolledSummary
            usage.text = item.usageSummary
            progress.progress = item.progressPercent

            val ctx = itemView.context
            when (item.status) {
                PromotionStatus.Active -> {
                    badge.setText(R.string.promo_status_active)
                    badge.setBackgroundResource(R.drawable.bg_badge_promo_active)
                    badge.setTextColor(ContextCompat.getColor(ctx, R.color.primary))
                }
                PromotionStatus.Paused -> {
                    badge.setText(R.string.promo_status_paused)
                    badge.setBackgroundResource(R.drawable.bg_badge_promo_paused)
                    badge.setTextColor(ContextCompat.getColor(ctx, R.color.text_secondary))
                }
            }

            val icon = itemView.findViewById<ImageView>(R.id.imageviewPromoIcon)
            if (item.useGiftIcon) {
                iconSlot.setBackgroundResource(R.drawable.bg_promo_icon_gift_rounded)
                icon.setImageDrawable(AppCompatResources.getDrawable(ctx, R.drawable.ic_card_gift_24))
                icon.imageTintList = AppCompatResources.getColorStateList(ctx, R.color.badge_gold_text)
            } else {
                iconSlot.setBackgroundResource(R.drawable.bg_promo_icon_rounded)
                icon.setImageDrawable(AppCompatResources.getDrawable(ctx, R.drawable.ic_heart_small_24))
                icon.imageTintList = AppCompatResources.getColorStateList(ctx, R.color.primary)
            }

            card.setOnClickListener { onItemClick(item) }

            itemView.findViewById<MaterialButton>(R.id.buttonPromoEdit).setOnClickListener {
                onSecondaryStub()
            }
            itemView.findViewById<MaterialButton>(R.id.buttonPromoPause).setOnClickListener {
                onSecondaryStub()
            }
            itemView.findViewById<TextView>(R.id.textviewPromoDetails).setOnClickListener {
                onSecondaryStub()
            }
        }
    }
}
