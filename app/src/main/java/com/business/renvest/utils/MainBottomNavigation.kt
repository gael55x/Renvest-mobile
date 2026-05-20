package com.business.renvest.utils

import android.content.Intent
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.data.local.localDataCounts
import com.business.renvest.screens.activityfeed.ActivityFeedActivity
import com.business.renvest.screens.customers.CustomersActivity
import com.business.renvest.screens.dashboard.DashboardActivity
import com.business.renvest.screens.profile.ProfileActivity
import com.business.renvest.screens.promotions.PromotionsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun AppCompatActivity.setupMainBottomNavigation(
    @IdRes selectedItemId: Int,
    scope: CoroutineScope,
) {
    scope.launch {
        val badge = withContext(Dispatchers.IO) { renvestDb().localDataCounts().activityEvents }
        withContext(Dispatchers.Main) {
            setupMainBottomNavigation(selectedItemId, badge)
        }
    }
}

fun AppCompatActivity.setupMainBottomNavigation(
    @IdRes selectedItemId: Int,
    activityBadgeCount: Int,
    clearTabSelection: Boolean = false,
) {
    val bottomnavigationMain = findViewById<BottomNavigationView>(R.id.bottomnavigationMain)
    if (clearTabSelection) {
        bottomnavigationMain.menu.setGroupCheckable(0, true, false)
    } else {
        bottomnavigationMain.menu.setGroupCheckable(0, true, true)
        bottomnavigationMain.selectedItemId = selectedItemId
    }
    val badge = bottomnavigationMain.getOrCreateBadge(R.id.navActivity)
    if (activityBadgeCount > 0) {
        badge.isVisible = true
        badge.number = activityBadgeCount
    } else {
        badge.isVisible = false
        badge.clearNumber()
    }
    bottomnavigationMain.setOnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navHome -> navigateMainTab(DashboardActivity::class.java)
            R.id.navCustomers -> navigateMainTab(CustomersActivity::class.java)
            R.id.navPromos -> navigateMainTab(PromotionsActivity::class.java)
            R.id.navActivity -> navigateMainTab(ActivityFeedActivity::class.java)
            R.id.navProfile -> navigateMainTab(ProfileActivity::class.java)
            else -> false
        }
    }
}

private fun AppCompatActivity.navigateMainTab(target: Class<*>): Boolean {
    if (this::class.java == target) return true
    startActivity(Intent(this, target))
    finish()
    return true
}
