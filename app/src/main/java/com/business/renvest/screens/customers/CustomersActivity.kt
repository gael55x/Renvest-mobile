package com.business.renvest.screens.customers

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.bindHeaderBusinessName
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast

class CustomersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_customers, R.id.root)
        bindHeaderBusinessName(R.id.text_header_business)

        setupMainBottomNavigation(R.id.nav_customers)

        val coming = getString(R.string.coming_soon)
        val stub = View.OnClickListener { toast(coming) }
        findViewById<View>(R.id.button_filter).setOnClickListener(stub)
        findViewById<View>(R.id.button_add_customer).setOnClickListener(stub)
        findViewById<View>(R.id.text_sort).setOnClickListener(stub)
        findViewById<View>(R.id.button_visit_maria).setOnClickListener(stub)
        findViewById<View>(R.id.button_pts_maria).setOnClickListener(stub)
        findViewById<View>(R.id.button_visit_john).setOnClickListener(stub)
        findViewById<View>(R.id.button_pts_john).setOnClickListener(stub)
        findViewById<View>(R.id.button_visit_sofia).setOnClickListener(stub)
        findViewById<View>(R.id.button_pts_sofia).setOnClickListener(stub)
    }
}
