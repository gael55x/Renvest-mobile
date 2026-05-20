package com.business.renvest.screens.promotions

import android.content.Context
import android.os.Bundle
import com.business.renvest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PromotionsPresenter(
    private val view: PromotionsContract.View,
    private val model: PromotionsModel,
    private val scope: CoroutineScope,
) : PromotionsContract.Presenter {

    private var filter: PromoFilter = PromoFilter.ALL

    override fun onViewReady(context: Context) {
        bindScreen(context)
    }

    override fun onTabSelected(context: Context, filter: PromoFilter) {
        this.filter = filter
        bindScreen(context)
    }

    fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) return
        filter = PromoFilter.entries[savedInstanceState.getInt(KEY_FILTER, PromoFilter.ALL.ordinal)]
        view.selectPromoFilter(filter)
    }

    fun saveState(outState: Bundle) {
        outState.putInt(KEY_FILTER, filter.ordinal)
    }

    override fun onNewPromoClicked(context: Context) {
        view.showNewPromotionDialog { title, reward, expiry, enrolled, redeemed ->
            onNewPromotionSubmitted(context, title, reward, expiry, enrolled, redeemed)
        }
    }

    override fun onNewPromotionSubmitted(
        context: Context,
        title: String,
        reward: String,
        expiry: String,
        enrolledCount: Int,
        redeemedCount: Int,
    ) {
        scope.launch {
            val ok = withContext(Dispatchers.IO) {
                model.addPromotion(context.applicationContext, title, reward, expiry, enrolledCount, redeemedCount)
            }
            withContext(Dispatchers.Main) {
                if (ok) {
                    model.markOnboardingPromotionStep(context)
                    bindScreen(context)
                    view.showToast(context.getString(R.string.promotion_added_confirmation))
                } else {
                    view.showToast(context.getString(R.string.error_field_required))
                }
            }
        }
    }

    override fun onPromotionPauseClicked(context: Context, item: PromotionItem) {
        scope.launch {
            withContext(Dispatchers.IO) { model.togglePromotionStatus(item.id, item.status) }
            withContext(Dispatchers.Main) {
                bindScreen(context)
                view.showToast(context.getString(R.string.promotion_status_updated))
            }
        }
    }

    override fun onPromotionEditClicked(context: Context, item: PromotionItem) {
        view.showEditPromotionDialog(item) { title, reward, expiry, enrolled, redeemed ->
            onPromotionEditSubmitted(context, item, title, reward, expiry, enrolled, redeemed)
        }
    }

    override fun onPromotionEditSubmitted(
        context: Context,
        item: PromotionItem,
        title: String,
        reward: String,
        expiry: String,
        enrolledCount: Int,
        redeemedCount: Int,
    ) {
        scope.launch {
            val ok = withContext(Dispatchers.IO) {
                model.updatePromotion(context.applicationContext, item, title, reward, expiry, enrolledCount, redeemedCount)
            }
            withContext(Dispatchers.Main) {
                if (ok) {
                    bindScreen(context)
                    view.showToast(context.getString(R.string.promotion_updated_confirmation))
                } else {
                    view.showToast(context.getString(R.string.error_field_required))
                }
            }
        }
    }

    override fun onPromotionRecordRedemptionClicked(context: Context, item: PromotionItem) {
        scope.launch {
            val ok = withContext(Dispatchers.IO) { model.recordRedemption(context.applicationContext, item) }
            withContext(Dispatchers.Main) {
                if (ok) {
                    bindScreen(context)
                    view.showToast(context.getString(R.string.promotion_redemption_recorded))
                } else {
                    view.showToast(context.getString(R.string.promotion_redemption_failed))
                }
            }
        }
    }

    override fun onPromotionLongPressed(context: Context, item: PromotionItem) {
        view.showDeletePromotionConfirm(item.title) {
            scope.launch {
                withContext(Dispatchers.IO) { model.deletePromotion(item.id) }
                withContext(Dispatchers.Main) { bindScreen(context) }
            }
        }
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }

    private fun bindScreen(context: Context) {
        scope.launch {
            val items = withContext(Dispatchers.IO) { model.loadPromotions(filter) }
            val counts = withContext(Dispatchers.IO) { model.localDataCounts() }
            withContext(Dispatchers.Main) {
                view.setHeaderBusinessName(model.businessDisplayName(context))
                view.setupNav(R.id.navPromos, counts.activityEvents)
                view.bindPromotionsHero(
                    activePromotions = counts.promotionsActive.toString(),
                    customerRecords = counts.customers.toString(),
                    activityRecords = counts.activityEvents.toString(),
                )
                view.displayPromotions(items)
                view.setPromotionsEmptyVisible(items.isEmpty())
            }
        }
    }

    companion object {
        private const val KEY_FILTER = "promo_filter"
    }
}
