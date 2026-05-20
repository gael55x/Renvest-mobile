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
            withContext(Dispatchers.Main) {
                view.setupNav(R.id.navHome, counts.activityEvents)
                if (counts.totalRows() == 0) {
                    view.bindLocalAdvisor(
                        title = context.getString(R.string.ai_advisor_local_title_not_enough),
                        body = context.getString(R.string.ai_advisor_local_body_not_enough),
                    )
                } else {
                    view.bindLocalAdvisor(
                        title = context.getString(R.string.ai_advisor_local_title_summary),
                        body = context.getString(
                            R.string.dashboard_ai_insight_summary_format,
                            counts.customers,
                            counts.promotions,
                            counts.loyaltyPrograms,
                            counts.loyaltyReminders,
                            counts.activityEvents,
                        ),
                    )
                }
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
