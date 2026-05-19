package com.business.renvest.screens.aiadvisor

import android.content.Context
import com.business.renvest.R
import com.business.renvest.screens.customers.CustomersActivity
import com.business.renvest.screens.promotions.PromotionsActivity

class AiEngagementAdvisorPresenter(
    private val view: AiEngagementAdvisorContract.View,
    private val model: AiEngagementAdvisorModel,
) : AiEngagementAdvisorContract.Presenter {

    override fun onViewReady(context: Context) {
        view.setHeaderBusinessName(model.businessDisplayName(context))
        view.setupNav(R.id.navHome)
        val counts = model.localDataCounts()
        if (counts.totalRows() == 0) {
            view.bindLocalAdvisor(
                title = context.getString(R.string.ai_advisor_local_title_not_enough),
                body = context.getString(R.string.ai_advisor_local_body_not_enough),
            )
        } else {
            val summary = context.getString(
                R.string.dashboard_ai_insight_summary_format,
                counts.customers,
                counts.promotions,
                counts.loyaltyPrograms,
                counts.loyaltyReminders,
                counts.activityEvents,
            )
            val disclaimer = context.getString(R.string.ai_advisor_local_disclaimer)
            view.bindLocalAdvisor(
                title = context.getString(R.string.ai_advisor_local_title_summary),
                body = "$summary\n\n$disclaimer",
            )
        }
    }

    override fun onActivatePromoClicked(context: Context) {
        view.navigateTo(PromotionsActivity::class.java)
    }

    override fun onViewDataClicked(context: Context) {
        view.navigateTo(CustomersActivity::class.java)
    }
}
