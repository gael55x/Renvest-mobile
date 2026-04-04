package com.business.renvest.screens.customers

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

class CustomersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_customers)
        applyEdgeToEdgeInsets(R.id.root)

        val stored = authRepository().getBusinessName(this).trim()
        findViewById<TextView>(R.id.text_header_business).text =
            if (stored.isNotEmpty()) stored else getString(R.string.default_business_display)

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
