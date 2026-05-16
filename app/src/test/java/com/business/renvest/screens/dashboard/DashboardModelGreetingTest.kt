package com.business.renvest.screens.dashboard

import com.business.renvest.R
import org.junit.Assert.assertEquals
import org.junit.Test

class DashboardModelGreetingTest {

    @Test
    fun greetingStringResForHour_morningEndsBeforeNoon() {
        assertEquals(R.string.greeting_morning, DashboardModel.greetingStringResForHour(0))
        assertEquals(R.string.greeting_morning, DashboardModel.greetingStringResForHour(11))
    }

    @Test
    fun greetingStringResForHour_afternoonStartsAtNoon() {
        assertEquals(R.string.greeting_afternoon, DashboardModel.greetingStringResForHour(12))
        assertEquals(R.string.greeting_afternoon, DashboardModel.greetingStringResForHour(16))
    }

    @Test
    fun greetingStringResForHour_eveningStartsAtFivePm() {
        assertEquals(R.string.greeting_evening, DashboardModel.greetingStringResForHour(17))
        assertEquals(R.string.greeting_evening, DashboardModel.greetingStringResForHour(23))
    }
}
