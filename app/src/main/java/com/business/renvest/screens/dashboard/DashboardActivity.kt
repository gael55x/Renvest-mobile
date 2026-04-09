package com.business.renvest.screens.dashboard

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.screens.aiadvisor.AiEngagementAdvisorActivity
import com.business.renvest.screens.customers.CustomersActivity
import com.business.renvest.screens.loyalty.LoyaltyActivity
import com.business.renvest.screens.profile.ProfileActivity
import com.business.renvest.screens.promotions.PromotionsActivity
import com.business.renvest.utils.displayBusinessName
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.startActivity
import com.business.renvest.utils.toast
import com.google.android.material.card.MaterialCardView
import java.util.Calendar

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_dashboard, R.id.root)

        val textGreeting = findViewById<TextView>(R.id.text_greeting)
        textGreeting.text = greetingForNow()

        val textViewBusinessName = findViewById<TextView>(R.id.text_business_name)
        textViewBusinessName.text = displayBusinessName()

        findViewById<android.view.View>(R.id.header_notification).setOnClickListener {
            toast(getString(R.string.coming_soon))
        }

        findViewById<TextView>(R.id.text_perf_view_report).setOnClickListener {
            toast(getString(R.string.coming_soon))
        }

        findViewById<MaterialCardView>(R.id.card_hero_revenue).setOnClickListener {
            /* static hero; no navigation */
        }

        findViewById<MaterialCardView>(R.id.perf_cell_members).setOnClickListener {
            startActivity(CustomersActivity::class.java)
        }

        findViewById<MaterialCardView>(R.id.perf_cell_rating).setOnClickListener {
            startActivity(LoyaltyActivity::class.java)
        }

        findViewById<MaterialCardView>(R.id.perf_cell_ticket).setOnClickListener {
            startActivity(PromotionsActivity::class.java)
        }

        findViewById<MaterialCardView>(R.id.perf_cell_churn).setOnClickListener {
            toast(getString(R.string.coming_soon))
        }

        findViewById<MaterialCardView>(R.id.card_ai_insight).setOnClickListener {
            startActivity(AiEngagementAdvisorActivity::class.java)
        }

        setupMainBottomNavigation(R.id.nav_home)
    }

    private fun greetingForNow(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            hour < 12 -> getString(R.string.greeting_morning)
            hour < 17 -> getString(R.string.greeting_afternoon)
            else -> getString(R.string.greeting_evening)
        }
    }
}
