package com.business.renvest.screens.customers

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.authStore
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast

class CustomersActivity : AppCompatActivity(), CustomersContract.View {

    private lateinit var presenter: CustomersPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_customers, R.id.root)

        presenter = CustomersPresenter(this, authStore())
        presenter.onViewReady(this)

        val stub = View.OnClickListener { presenter.onStubInteraction() }
        findViewById<View>(R.id.buttonFilter).setOnClickListener(stub)
        findViewById<View>(R.id.buttonAddCustomer).setOnClickListener(stub)
        findViewById<View>(R.id.textviewSort).setOnClickListener(stub)
        findViewById<View>(R.id.buttonVisitMaria).setOnClickListener(stub)
        findViewById<View>(R.id.buttonPtsMaria).setOnClickListener(stub)
        findViewById<View>(R.id.buttonVisitJohn).setOnClickListener(stub)
        findViewById<View>(R.id.buttonPtsJohn).setOnClickListener(stub)
        findViewById<View>(R.id.buttonVisitSofia).setOnClickListener(stub)
        findViewById<View>(R.id.buttonPtsSofia).setOnClickListener(stub)
    }

    override fun setHeaderBusinessName(text: String) {
        findViewById<TextView>(R.id.textviewHeaderBusiness).text = text
    }

    override fun setupNav(selectedItemId: Int) {
        setupMainBottomNavigation(selectedItemId)
    }

    override fun showComingSoon() {
        toast(getString(R.string.coming_soon))
    }
}
