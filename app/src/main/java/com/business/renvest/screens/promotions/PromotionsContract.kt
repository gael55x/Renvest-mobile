package com.business.renvest.screens.promotions

import android.content.Context
import androidx.annotation.IdRes

interface PromotionsContract {
    interface View {
        fun setHeaderBusinessName(text: String)
        fun setupNav(@IdRes selectedItemId: Int, activityBadgeCount: Int)
        fun bindPromotionsHero(activePromotions: String, customerRecords: String, activityRecords: String)
        fun displayPromotions(items: List<PromotionItem>)
        fun setPromotionsEmptyVisible(visible: Boolean)
        fun showToast(message: String)
        fun showNewPromotionDialog(onSubmit: (String, String, String) -> Unit)
        fun showEditPromotionDialog(item: PromotionItem, onSubmit: (String, String, String) -> Unit)
        fun showDeletePromotionConfirm(title: String, onConfirm: () -> Unit)
        fun showComingSoon()
    }

    interface Presenter {
        fun onViewReady(context: Context)
        fun onTabSelected(context: Context, filter: PromoFilter)
        fun onNewPromoClicked(context: Context)
        fun onNewPromotionSubmitted(context: Context, title: String, reward: String, expiry: String)
        fun onPromotionPauseClicked(context: Context, item: PromotionItem)
        fun onPromotionEditClicked(context: Context, item: PromotionItem)
        fun onPromotionEditSubmitted(context: Context, item: PromotionItem, title: String, reward: String, expiry: String)
        fun onPromotionLongPressed(context: Context, item: PromotionItem)
        fun onStubInteraction()
    }
}
