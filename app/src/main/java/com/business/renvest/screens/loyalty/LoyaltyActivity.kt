package com.business.renvest.screens.loyalty

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.setupRenvestContent

class LoyaltyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_feature_stub, R.id.root)
        findViewById<TextView>(R.id.text_stub_title).setText(R.string.feature_loyalty_title)
    }
}
