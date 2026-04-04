package com.business.renvest.screens.promotions

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.applyEdgeToEdgeInsets
import com.business.renvest.utils.authRepository
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.toast

class PromotionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_promotions)
        applyEdgeToEdgeInsets(R.id.root)

        val stored = authRepository().getBusinessName(this).trim()
        findViewById<TextView>(R.id.text_header_business).text =
            if (stored.isNotEmpty()) stored else getString(R.string.default_business_display)

        setupMainBottomNavigation(R.id.nav_promos)

        val coming = getString(R.string.coming_soon)
        val stub = View.OnClickListener { toast(coming) }
        findViewById<View>(R.id.button_new_promo).setOnClickListener(stub)
        findViewById<View>(R.id.button_promo1_edit).setOnClickListener(stub)
        findViewById<View>(R.id.button_promo1_pause).setOnClickListener(stub)
        findViewById<View>(R.id.text_promo1_details).setOnClickListener(stub)
        findViewById<View>(R.id.button_promo2_edit).setOnClickListener(stub)
        findViewById<View>(R.id.button_promo2_pause).setOnClickListener(stub)
        findViewById<View>(R.id.text_promo2_details).setOnClickListener(stub)
    }
}
