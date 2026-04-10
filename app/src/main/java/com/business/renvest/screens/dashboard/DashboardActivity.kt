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

        findViewById<android.view.View>(R.id.framelayoutHeaderNotification).setOnClickListener {
            presenter.onNotificationClicked()
        }

        findViewById<TextView>(R.id.textviewPerfViewReport).setOnClickListener {
            presenter.onPerfViewReportClicked()
        }

        findViewById<MaterialCardView>(R.id.materialcardHeroRevenue).setOnClickListener {
            /* static hero; no navigation */
        }

        findViewById<MaterialCardView>(R.id.materialcardPerfCellMembers).setOnClickListener {
            presenter.onPerfCellMembersClicked()
        }

        findViewById<MaterialCardView>(R.id.materialcardPerfCellRating).setOnClickListener {
            presenter.onPerfCellRatingClicked()
        }

        findViewById<MaterialCardView>(R.id.materialcardPerfCellTicket).setOnClickListener {
            presenter.onPerfCellTicketClicked()
        }

        findViewById<MaterialCardView>(R.id.materialcardPerfCellChurn).setOnClickListener {
            presenter.onPerfCellChurnClicked()
        }

        findViewById<MaterialCardView>(R.id.materialcardAiInsight).setOnClickListener {
            presenter.onCardAiInsightClicked()
        }

        setupMainBottomNavigation(R.id.navHome)
    }

    override fun setGreeting(text: String) {
        findViewById<TextView>(R.id.textviewGreeting).text = text
    }

    override fun setBusinessName(text: String) {
        findViewById<TextView>(R.id.textviewBusinessName).text = text
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
