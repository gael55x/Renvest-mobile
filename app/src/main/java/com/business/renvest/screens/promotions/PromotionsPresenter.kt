package com.business.renvest.screens.promotions

import android.content.Context
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

    override fun onViewReady(context: Context) {
        bindScreen(context)
    }

    override fun onNewPromoClicked(context: Context) {
        view.showNewPromotionDialog { title, reward, expiry ->
            onNewPromotionSubmitted(context, title, reward, expiry)
        }
    }

    override fun onNewPromotionSubmitted(context: Context, title: String, reward: String, expiry: String) {
        scope.launch {
            val ok = withContext(Dispatchers.IO) {
                model.addPromotionMinimal(context.applicationContext, title, reward, expiry)
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
        view.showEditPromotionDialog(item) { title, reward, expiry ->
            onPromotionEditSubmitted(context, item, title, reward, expiry)
        }
    }

    override fun onPromotionEditSubmitted(
        context: Context,
        item: PromotionItem,
        title: String,
        reward: String,
        expiry: String,
    ) {
        scope.launch {
            val ok = withContext(Dispatchers.IO) { model.updatePromotion(item, title, reward, expiry) }
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
            val items = withContext(Dispatchers.IO) { model.loadPromotions() }
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
}
