package com.business.renvest.screens.dashboard

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.screens.aiadvisor.AiEngagementAdvisorActivity
import com.business.renvest.screens.customers.CustomersActivity
import com.business.renvest.screens.loyalty.LoyaltyActivity
import com.business.renvest.screens.promotions.PromotionsActivity
import com.business.renvest.utils.authStore
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.startActivity
import com.business.renvest.utils.toast
import com.google.android.material.card.MaterialCardView

class DashboardActivity : AppCompatActivity(), DashboardContract.View {

    private lateinit var presenter: DashboardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_dashboard, R.id.root)

        presenter = DashboardPresenter(this, authStore())
        presenter.onViewReady(this)

        findViewById<android.view.View>(R.id.header_notification).setOnClickListener {
            presenter.onNotificationClicked()
        }

        findViewById<TextView>(R.id.text_perf_view_report).setOnClickListener {
            presenter.onPerfViewReportClicked()
        }

        findViewById<MaterialCardView>(R.id.card_hero_revenue).setOnClickListener {
            /* static hero; no navigation */
        }

        findViewById<MaterialCardView>(R.id.perf_cell_members).setOnClickListener {
            presenter.onPerfCellMembersClicked()
        }

        findViewById<MaterialCardView>(R.id.perf_cell_rating).setOnClickListener {
            presenter.onPerfCellRatingClicked()
        }

        findViewById<MaterialCardView>(R.id.perf_cell_ticket).setOnClickListener {
            presenter.onPerfCellTicketClicked()
        }

        findViewById<MaterialCardView>(R.id.perf_cell_churn).setOnClickListener {
            presenter.onPerfCellChurnClicked()
        }

        findViewById<MaterialCardView>(R.id.card_ai_insight).setOnClickListener {
            presenter.onCardAiInsightClicked()
        }

        setupMainBottomNavigation(R.id.nav_home)
    }

    override fun setGreeting(text: String) {
        findViewById<TextView>(R.id.text_greeting).text = text
    }

    override fun setBusinessName(text: String) {
        findViewById<TextView>(R.id.text_business_name).text = text
    }

    override fun showComingSoon() {
        toast(getString(R.string.coming_soon))
    }

    override fun navigateToCustomers() {
        startActivity(CustomersActivity::class.java)
    }

    override fun navigateToLoyalty() {
        startActivity(LoyaltyActivity::class.java)
    }

    override fun navigateToPromotions() {
        startActivity(PromotionsActivity::class.java)
    }

    override fun navigateToAiAdvisor() {
        startActivity(AiEngagementAdvisorActivity::class.java)
    }
}
