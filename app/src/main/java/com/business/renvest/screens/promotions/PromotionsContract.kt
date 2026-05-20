package com.business.renvest.screens.promotions

import android.content.Context
import androidx.annotation.IdRes

typealias PromotionFormSubmit = (
    title: String,
    reward: String,
    expiry: String,
    enrolledCount: Int,
    redeemedCount: Int,
) -> Unit

interface PromotionsContract {
    interface View {
        fun setHeaderBusinessName(text: String)
        fun setupNav(@IdRes selectedItemId: Int, activityBadgeCount: Int)
        fun bindPromotionsHero(activePromotions: String, customerRecords: String, activityRecords: String)
        fun displayPromotions(items: List<PromotionItem>)
        fun setPromotionsEmptyVisible(visible: Boolean)
        fun showToast(message: String)
        fun showNewPromotionDialog(onSubmit: PromotionFormSubmit)
        fun showEditPromotionDialog(item: PromotionItem, onSubmit: PromotionFormSubmit)
        fun showDeletePromotionConfirm(title: String, onConfirm: () -> Unit)
        fun showComingSoon()
        fun selectPromoFilter(filter: PromoFilter)
    }

    interface Presenter {
        fun onViewReady(context: Context)
        fun onTabSelected(context: Context, filter: PromoFilter)
        fun onNewPromoClicked(context: Context)
        fun onNewPromotionSubmitted(
            context: Context,
            title: String,
            reward: String,
            expiry: String,
            enrolledCount: Int,
            redeemedCount: Int,
        )
        fun onPromotionPauseClicked(context: Context, item: PromotionItem)
        fun onPromotionEditClicked(context: Context, item: PromotionItem)
        fun onPromotionEditSubmitted(
            context: Context,
            item: PromotionItem,
            title: String,
            reward: String,
            expiry: String,
            enrolledCount: Int,
            redeemedCount: Int,
        )
        fun onPromotionRecordRedemptionClicked(context: Context, item: PromotionItem)
        fun onPromotionLongPressed(context: Context, item: PromotionItem)
        fun onStubInteraction()
    }
}
