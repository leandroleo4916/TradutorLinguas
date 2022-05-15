package com.example.tradutorlinguas.util

import org.junit.Test

import org.junit.Assert.*

class FormatTextTest {

    @Test
    fun formatText() {
        val cap = FormatText()
        val con = cap.formatText("I don&#39;t care")
        assertEquals("I don't care", con)
    }
}