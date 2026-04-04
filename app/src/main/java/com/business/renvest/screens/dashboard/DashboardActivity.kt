package com.business.renvest.screens.dashboard

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.authRepository
import com.business.renvest.screens.customers.CustomersActivity
import com.business.renvest.screens.loyalty.LoyaltyActivity
import com.business.renvest.screens.profile.ProfileActivity
import com.business.renvest.screens.promotions.PromotionsActivity
import com.business.renvest.utils.applyEdgeToEdgeInsets
import com.business.renvest.utils.startActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        applyEdgeToEdgeInsets(R.id.root)

        val textViewBusinessName = findViewById<TextView>(R.id.text_business_name)
        val stringBusinessStored = authRepository().getBusinessName(this).trim()
        textViewBusinessName.text =
            if (stringBusinessStored.isNotEmpty()) {
                stringBusinessStored
            } else {
                getString(R.string.default_business_display)
            }

        val materialButtonManageLoyalty = findViewById<MaterialButton>(R.id.button_manage_loyalty)
        val materialButtonOpenProfile = findViewById<MaterialButton>(R.id.button_profile)

        materialButtonManageLoyalty.setOnClickListener {
            startActivity(LoyaltyActivity::class.java)
        }

        findViewById<MaterialCardView>(R.id.card_metric_customers).setOnClickListener {
            startActivity(CustomersActivity::class.java)
        }

        findViewById<MaterialCardView>(R.id.card_metric_points).setOnClickListener {
            startActivity(LoyaltyActivity::class.java)
        }

        findViewById<MaterialCardView>(R.id.card_metric_promos).setOnClickListener {
            startActivity(PromotionsActivity::class.java)
        }

        materialButtonOpenProfile.setOnClickListener {
            startActivity(ProfileActivity::class.java)
        }
    }
}
