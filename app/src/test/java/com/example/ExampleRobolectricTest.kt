package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.config.AppConfig
import com.example.config.StoreRegion
import com.example.utils.BraSizeCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `verify app name and soft care guide strings`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("FemCare 360", appName)

        val tagline = context.getString(R.string.app_tagline)
        assertTrue(tagline.contains("Soft Care Guide"))
    }

    @Test
    fun `verify 78cm underbust and 88cm bust gives 34B`() {
        val braResult = BraSizeCalculator.calculateBraSize(78.0, 88.0, "cm")
        assertEquals("34B", braResult.fullSize)
        assertEquals(34, braResult.bandSize)
        assertEquals("B", braResult.cupSize)
        assertEquals(listOf("32B", "36B"), braResult.sisterSizes)

        val fit = AppConfig.calculateBraSize(78f, 88f, isCm = true)
        assertNotNull(fit)
        assertEquals("34B", fit?.fullSize)

        val saLinks = AppConfig.getStoreLinksForRegion(StoreRegion.SAUDI_ARABIA, "34B")
        assertEquals(2, saLinks.size)
        assertTrue(saLinks[0].url.contains("amazon.sa"))
        assertTrue(saLinks[1].url.contains("noon.com"))

        val bdLinks = AppConfig.getStoreLinksForRegion(StoreRegion.BANGLADESH, "34B")
        assertEquals(1, bdLinks.size)
        assertTrue(bdLinks[0].url.contains("daraz.com.bd"))
    }
}
