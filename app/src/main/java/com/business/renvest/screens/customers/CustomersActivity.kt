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
import com.business.renvest.utils.toastComingSoon

class CustomersActivity : AppCompatActivity(), CustomersContract.View {

    private lateinit var presenter: CustomersPresenter
    private lateinit var textviewHeaderBusiness: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_customers, R.id.root)

        textviewHeaderBusiness = findViewById(R.id.textviewHeaderBusiness)

        presenter = CustomersPresenter(this, CustomersModel(authStore()))
        presenter.onViewReady(this)

        val stub = View.OnClickListener { presenter.onStubInteraction() }
        val buttonFilter = findViewById<View>(R.id.buttonFilter)
        val buttonAddCustomer = findViewById<View>(R.id.buttonAddCustomer)
        val textviewSort = findViewById<TextView>(R.id.textviewSort)
        val buttonVisitMaria = findViewById<View>(R.id.buttonVisitMaria)
        val buttonPtsMaria = findViewById<View>(R.id.buttonPtsMaria)
        val buttonVisitJohn = findViewById<View>(R.id.buttonVisitJohn)
        val buttonPtsJohn = findViewById<View>(R.id.buttonPtsJohn)
        val buttonVisitSofia = findViewById<View>(R.id.buttonVisitSofia)
        val buttonPtsSofia = findViewById<View>(R.id.buttonPtsSofia)

        buttonFilter.setOnClickListener(stub)
        buttonAddCustomer.setOnClickListener(stub)
        textviewSort.setOnClickListener(stub)
        buttonVisitMaria.setOnClickListener(stub)
        buttonPtsMaria.setOnClickListener(stub)
        buttonVisitJohn.setOnClickListener(stub)
        buttonPtsJohn.setOnClickListener(stub)
        buttonVisitSofia.setOnClickListener(stub)
        buttonPtsSofia.setOnClickListener(stub)
    }

    override fun setHeaderBusinessName(text: String) {
        textviewHeaderBusiness.text = text
    }

    override fun setupNav(selectedItemId: Int) {
        setupMainBottomNavigation(selectedItemId)
    }

    override fun showComingSoon() {
        toastComingSoon()
    }
}
