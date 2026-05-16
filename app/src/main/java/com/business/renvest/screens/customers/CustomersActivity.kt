package com.business.renvest.screens.customers

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.business.renvest.R
import com.business.renvest.utils.authStore
import com.business.renvest.utils.renvestDb
import com.business.renvest.utils.setClickListeners
import com.business.renvest.utils.setTextViewText
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toastComingSoon

class CustomersActivity : AppCompatActivity(), CustomersContract.View {

    private lateinit var presenter: CustomersPresenter
    private lateinit var customersAdapter: CustomersAdapter
    private lateinit var textviewCustomersEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_customers, R.id.root)

        textviewCustomersEmpty = findViewById(R.id.textviewCustomersEmpty)
        customersAdapter = CustomersAdapter()
        findViewById<RecyclerView>(R.id.recyclerviewCustomers).apply {
            layoutManager = LinearLayoutManager(this@CustomersActivity)
            adapter = customersAdapter
        }

        presenter = CustomersPresenter(this, CustomersModel(authStore(), renvestDb()))
        presenter.onViewReady(this)

        val stub = View.OnClickListener { presenter.onStubInteraction() }
        setClickListeners(
            stub,
            R.id.buttonFilter,
            R.id.buttonAddCustomer,
            R.id.textviewSort,
        )
    }

    override fun setHeaderBusinessName(text: String) {
        setTextViewText(R.id.textviewHeaderBusiness, text)
    }

    override fun setupNav(selectedItemId: Int) {
        setupMainBottomNavigation(selectedItemId)
    }

    override fun bindHeroMetrics(members: String, returnOrPlaceholder: String, atRisk: String) {
        findViewById<TextView>(R.id.textviewCustomersHeroMembers).text = members
        findViewById<TextView>(R.id.textviewCustomersHeroReturn).text = returnOrPlaceholder
        findViewById<TextView>(R.id.textviewCustomersHeroAtRisk).text = atRisk
    }

    override fun bindCustomerRows(items: List<CustomerRowUi>) {
        customersAdapter.submitList(items)
    }

    override fun setCustomersEmptyVisible(visible: Boolean) {
        textviewCustomersEmpty.isVisible = visible
    }

    override fun showComingSoon() {
        toastComingSoon()
    }
}
