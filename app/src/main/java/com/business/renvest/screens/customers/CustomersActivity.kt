package com.business.renvest.screens.customers

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.authRepository
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast

class CustomersActivity : AppCompatActivity(), CustomersContract.View {

    private lateinit var presenter: CustomersPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_customers, R.id.root)

        presenter = CustomersPresenter(this, CustomersModel(authRepository()))
        presenter.onViewReady(this)

        val stub = View.OnClickListener { presenter.onStubInteraction() }
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

    override fun setHeaderBusinessName(text: String) {
        findViewById<TextView>(R.id.text_header_business).text = text
    }

    override fun setupNav(selectedItemId: Int) {
        setupMainBottomNavigation(selectedItemId)
    }

    override fun showComingSoon() {
        toast(getString(R.string.coming_soon))
    }
}
