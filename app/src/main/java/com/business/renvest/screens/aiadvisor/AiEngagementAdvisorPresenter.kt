package com.business.renvest.screens.aiadvisor

import android.content.Context
import com.business.renvest.R
import com.business.renvest.screens.customers.CustomersActivity
import com.business.renvest.screens.promotions.PromotionsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AiEngagementAdvisorPresenter(
    private val view: AiEngagementAdvisorContract.View,
    private val model: AiEngagementAdvisorModel,
    private val scope: CoroutineScope,
) : AiEngagementAdvisorContract.Presenter {

    override fun onViewReady(context: Context) {
        view.setHeaderBusinessName(model.businessDisplayName(context))
        scope.launch {
            val counts = withContext(Dispatchers.IO) { model.localDataCounts() }
            val insight = withContext(Dispatchers.IO) { model.advisorInsight(context) }
            withContext(Dispatchers.Main) {
                view.setupNav(R.id.navHome, counts.activityEvents)
                view.bindLocalAdvisor(insight.title, insight.body)
            }
        }
    }

    override fun onActivatePromoClicked(context: Context) {
        view.navigateTo(PromotionsActivity::class.java)
    }

    override fun onViewDataClicked(context: Context) {
        view.navigateTo(CustomersActivity::class.java)
    }
}
