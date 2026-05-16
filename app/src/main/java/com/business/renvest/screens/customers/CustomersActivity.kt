package com.business.renvest.screens.customers

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.authStore
import com.business.renvest.utils.setClickListeners
import com.business.renvest.utils.setTextViewText
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast
import com.business.renvest.utils.toastComingSoon

class CustomersActivity : AppCompatActivity(), CustomersContract.View {

    private lateinit var presenter: CustomersPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_customers, R.id.root)

        presenter = CustomersPresenter(this, CustomersModel(authStore()))
        presenter.onViewReady(this)

        val stub = View.OnClickListener { presenter.onStubInteraction() }
        setClickListeners(
            stub,
            R.id.buttonFilter,
            R.id.buttonAddCustomer,
            R.id.textviewSort,
            R.id.buttonVisitMaria,
            R.id.buttonPtsMaria,
            R.id.buttonVisitJohn,
            R.id.buttonPtsJohn,
            R.id.buttonVisitSofia,
            R.id.buttonPtsSofia,
        )
    }

    override fun setHeaderBusinessName(text: String) {
        setTextViewText(R.id.textviewHeaderBusiness, text)
    }

    override fun setupNav(selectedItemId: Int) {
        setupMainBottomNavigation(selectedItemId)
    }

    override fun showComingSoon() {
        toastComingSoon()
    }
}
