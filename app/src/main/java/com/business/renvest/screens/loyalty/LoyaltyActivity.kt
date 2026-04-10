package com.business.renvest.screens.loyalty

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.setupRenvestContent

class LoyaltyActivity : AppCompatActivity(), LoyaltyContract.View {

    private lateinit var presenter: LoyaltyPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_feature_stub, R.id.root)
        presenter = LoyaltyPresenter(this)
        presenter.onViewReady()
    }

    override fun setStubTitle(titleResId: Int) {
        findViewById<TextView>(R.id.text_stub_title).setText(titleResId)
    }
}
