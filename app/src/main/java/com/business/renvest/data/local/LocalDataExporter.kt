package com.business.renvest.data.local

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/** Exports local Room rows to a JSON file under app cache (no network). */
class LocalDataExporter(private val db: RenvestDatabase) {

    fun exportToCacheFile(context: Context): File {
        val customers = JSONArray()
        db.customerDao().listAll().forEach { c ->
            customers.put(
                JSONObject()
                    .put("id", c.id)
                    .put("displayName", c.displayName)
                    .put("createdAt", c.createdAt)
                    .put("updatedAt", c.updatedAt),
            )
        }
        val promotions = JSONArray()
        db.promotionDao().listAll().forEach { p ->
            promotions.put(
                JSONObject()
                    .put("id", p.id)
                    .put("title", p.title)
                    .put("reward", p.reward)
                    .put("expiry", p.expiry)
                    .put("status", p.status)
                    .put("createdAt", p.createdAt),
            )
        }
        val programs = JSONArray()
        db.loyaltyProgramDao().listAll().forEach { lp ->
            programs.put(
                JSONObject()
                    .put("id", lp.id)
                    .put("name", lp.name)
                    .put("visitsRequired", lp.visitsRequired)
                    .put("rewardDescription", lp.rewardDescription),
            )
        }
        val reminders = JSONArray()
        db.loyaltyReminderDao().listAll().forEach { r ->
            reminders.put(
                JSONObject()
                    .put("id", r.id)
                    .put("title", r.title)
                    .put("subtitle", r.subtitle),
            )
        }
        val events = JSONArray()
        db.activityEventDao().listAll().forEach { e ->
            events.put(
                JSONObject()
                    .put("id", e.id)
                    .put("title", e.title)
                    .put("subtitle", e.subtitle)
                    .put("customerId", e.customerId)
                    .put("createdAt", e.createdAt),
            )
        }
        val root = JSONObject()
            .put("exportedAt", System.currentTimeMillis())
            .put("customers", customers)
            .put("promotions", promotions)
            .put("loyaltyPrograms", programs)
            .put("loyaltyReminders", reminders)
            .put("activityEvents", events)
        val file = File(context.cacheDir, "renvest_export_${System.currentTimeMillis()}.json")
        file.writeText(root.toString(2))
        return file
    }
}
