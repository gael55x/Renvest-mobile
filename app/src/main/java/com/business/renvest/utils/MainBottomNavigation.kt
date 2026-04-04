package com.business.renvest.utils

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.screens.activityfeed.ActivityFeedActivity
import com.business.renvest.screens.customers.CustomersActivity
import com.business.renvest.screens.dashboard.DashboardActivity
import com.business.renvest.screens.profile.ProfileActivity
import com.business.renvest.screens.promotions.PromotionsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

fun AppCompatActivity.setupMainBottomNavigation(@IdRes selectedItemId: Int) {
    val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
    bottomNav.selectedItemId = selectedItemId
    bottomNav.getOrCreateBadge(R.id.nav_activity).apply {
        isVisible = true
        number = 3
    }
    bottomNav.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> navigateMainTab(DashboardActivity::class.java, selectedItemId == R.id.nav_home)
            R.id.nav_customers -> navigateMainTab(CustomersActivity::class.java, selectedItemId == R.id.nav_customers)
            R.id.nav_promos -> navigateMainTab(PromotionsActivity::class.java, selectedItemId == R.id.nav_promos)
            R.id.nav_activity -> navigateMainTab(ActivityFeedActivity::class.java, selectedItemId == R.id.nav_activity)
            R.id.nav_profile -> navigateMainTab(ProfileActivity::class.java, selectedItemId == R.id.nav_profile)
            else -> false
        }
    })
}

private fun AppCompatActivity.navigateMainTab(target: Class<*>, alreadySelected: Boolean): Boolean {
    if (alreadySelected) return true
    startActivity(target)
    return true
}
