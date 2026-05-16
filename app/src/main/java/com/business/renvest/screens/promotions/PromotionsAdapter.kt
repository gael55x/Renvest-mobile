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
    private val onEditClick: (PromotionItem) -> Unit,
    private val onPauseClick: (PromotionItem) -> Unit,
    private val onDetailsClick: (PromotionItem) -> Unit,
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

        private val materialcardPromotionRow: MaterialCardView =
            itemView.findViewById(R.id.materialcardPromotionRow)
        private val framelayoutPromoIcon: View = itemView.findViewById(R.id.framelayoutPromoIcon)
        private val textviewPromoBadge: TextView = itemView.findViewById(R.id.textviewPromoBadge)
        private val textviewPromoTitle: TextView = itemView.findViewById(R.id.textviewPromoTitle)
        private val textviewPromoReward: TextView = itemView.findViewById(R.id.textviewPromoReward)
        private val textviewPromoExpiry: TextView = itemView.findViewById(R.id.textviewPromoExpiry)
        private val textviewPromoEnrolled: TextView = itemView.findViewById(R.id.textviewPromoEnrolled)
        private val textviewPromoUsage: TextView = itemView.findViewById(R.id.textviewPromoUsage)
        private val progressbarPromo: ProgressBar = itemView.findViewById(R.id.progressPromo)
        private val imageviewPromoIcon: ImageView = itemView.findViewById(R.id.imageviewPromoIcon)
        private val materialbuttonPromoEdit: MaterialButton =
            itemView.findViewById(R.id.buttonPromoEdit)
        private val materialbuttonPromoPause: MaterialButton =
            itemView.findViewById(R.id.buttonPromoPause)
        private val textviewPromoDetails: TextView = itemView.findViewById(R.id.textviewPromoDetails)

        fun bind(item: PromotionItem) {
            textviewPromoTitle.text = item.title
            textviewPromoReward.text = item.reward
            textviewPromoExpiry.text = item.expiry
            textviewPromoEnrolled.text = item.enrolledSummary
            textviewPromoUsage.text = item.usageSummary
            progressbarPromo.progress = item.progressPercent

            bindPromoBadge(item.status)

            val ctx = itemView.context
            if (item.useGiftIcon) {
                framelayoutPromoIcon.setBackgroundResource(R.drawable.bg_promo_icon_gift_rounded)
                imageviewPromoIcon.setImageDrawable(AppCompatResources.getDrawable(ctx, R.drawable.ic_card_gift_24))
                imageviewPromoIcon.imageTintList = AppCompatResources.getColorStateList(ctx, R.color.badge_gold_text)
            } else {
                framelayoutPromoIcon.setBackgroundResource(R.drawable.bg_promo_icon_rounded)
                imageviewPromoIcon.setImageDrawable(AppCompatResources.getDrawable(ctx, R.drawable.ic_heart_small_24))
                imageviewPromoIcon.imageTintList = AppCompatResources.getColorStateList(ctx, R.color.primary)
            }

            materialcardPromotionRow.setOnClickListener { onItemClick(item) }

            materialbuttonPromoEdit.setOnClickListener {
                onEditClick(item)
            }
            materialbuttonPromoPause.setOnClickListener {
                onPauseClick(item)
            }
            textviewPromoDetails.setOnClickListener {
                onDetailsClick(item)
            }
        }

        private fun bindPromoBadge(status: PromotionStatus) {
            val ctx = itemView.context
            when (status) {
                PromotionStatus.Active -> {
                    textviewPromoBadge.setText(R.string.promo_status_active)
                    textviewPromoBadge.setBackgroundResource(R.drawable.bg_badge_promo_active)
                    textviewPromoBadge.setTextColor(ContextCompat.getColor(ctx, R.color.primary))
                }
                PromotionStatus.Paused -> {
                    textviewPromoBadge.setText(R.string.promo_status_paused)
                    textviewPromoBadge.setBackgroundResource(R.drawable.bg_badge_promo_paused)
                    textviewPromoBadge.setTextColor(ContextCompat.getColor(ctx, R.color.text_secondary))
                }
            }
        }
    }
}
