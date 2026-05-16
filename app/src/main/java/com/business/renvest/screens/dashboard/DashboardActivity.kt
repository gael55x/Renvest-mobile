package com.business.renvest.screens.dashboard

import android.os.Bundle
import android.view.View
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
import com.business.renvest.utils.toastComingSoon
import com.google.android.material.card.MaterialCardView

class DashboardActivity : AppCompatActivity(), DashboardContract.View {

    private lateinit var presenter: DashboardPresenter
    private lateinit var textviewGreeting: TextView
    private lateinit var textviewBusinessName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_dashboard, R.id.root)

        textviewGreeting = findViewById(R.id.textviewGreeting)
        textviewBusinessName = findViewById(R.id.textviewBusinessName)

        presenter = DashboardPresenter(this, DashboardModel(authStore()))
        presenter.onViewReady(this)

        val framelayoutHeaderNotification = findViewById<View>(R.id.framelayoutHeaderNotification)
        val textviewPerfViewReport = findViewById<TextView>(R.id.textviewPerfViewReport)
        val materialcardHeroRevenue = findViewById<MaterialCardView>(R.id.materialcardHeroRevenue)
        val materialcardPerfCellMembers = findViewById<MaterialCardView>(R.id.materialcardPerfCellMembers)
        val materialcardPerfCellRating = findViewById<MaterialCardView>(R.id.materialcardPerfCellRating)
        val materialcardPerfCellTicket = findViewById<MaterialCardView>(R.id.materialcardPerfCellTicket)
        val materialcardPerfCellChurn = findViewById<MaterialCardView>(R.id.materialcardPerfCellChurn)
        val materialcardAiInsight = findViewById<MaterialCardView>(R.id.materialcardAiInsight)

        framelayoutHeaderNotification.setOnClickListener {
            presenter.onNotificationClicked()
        }

        textviewPerfViewReport.setOnClickListener {
            presenter.onPerfViewReportClicked()
        }

        materialcardHeroRevenue.setOnClickListener {
            /* static hero; no navigation */
        }

        materialcardPerfCellMembers.setOnClickListener {
            presenter.onPerfCellMembersClicked()
        }

        materialcardPerfCellRating.setOnClickListener {
            presenter.onPerfCellRatingClicked()
        }

        materialcardPerfCellTicket.setOnClickListener {
            presenter.onPerfCellTicketClicked()
        }

        materialcardPerfCellChurn.setOnClickListener {
            presenter.onPerfCellChurnClicked()
        }

        materialcardAiInsight.setOnClickListener {
            presenter.onCardAiInsightClicked()
        }

        setupMainBottomNavigation(R.id.navHome)
    }

    override fun setGreeting(text: String) {
        textviewGreeting.text = text
    }

    override fun setBusinessName(text: String) {
        textviewBusinessName.text = text
    }

    override fun showComingSoon() {
        toastComingSoon()
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
