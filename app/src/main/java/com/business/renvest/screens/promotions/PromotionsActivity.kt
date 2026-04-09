package com.business.renvest.screens.promotions

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.bindHeaderBusinessName
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast

class PromotionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_promotions, R.id.root)
        bindHeaderBusinessName(R.id.text_header_business)

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
