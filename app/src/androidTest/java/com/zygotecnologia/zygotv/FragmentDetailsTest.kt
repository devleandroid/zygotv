package com.zygotecnologia.zygotv

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.zygotecnologia.zygotv.main.MainActivity
import com.zygotecnologia.zygotv.main.MainAdapter
import org.hamcrest.Matchers
import org.hamcrest.core.AllOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FragmentDetailsTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun infoIsShownCorrectly() {

        Espresso.onView(withId(R.id.rv_show_list))
            .perform(RecyclerViewActions.scrollToPosition<MainAdapter.ViewHolder>(0))
            .perform(ViewActions.click())

        checkTextIsNotEmpty(R.id.name)
        checkTextIsNotEmpty(R.id.overview)
        checkTextIsNotEmpty(R.id.voteAverage)
        checkImageIsDisplayed(R.id.poster)
        checkImageIsDisplayed(R.id.toggleButton)
    }

    private fun checkTextIsNotEmpty(id: Int) {

        Espresso.onView(withId(id)).check(
            ViewAssertions.matches(
                Matchers.not(
                    ViewMatchers.withInputType(id)
                )
            )
        )
    }

    private fun checkImageIsDisplayed(id: Int) {
        Espresso.onView(
            AllOf.allOf(
                withId(id),
                ViewMatchers.isDescendantOfA(withId(R.id.posterDetails))
            )
        ).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    inline fun waitUntilLoaded(crossinline recyclerProvider: () -> RecyclerView) {
        Espresso.onIdle()

        lateinit var recycler: RecyclerView

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            recycler = recyclerProvider()
        }

        while (recycler.hasPendingAdapterUpdates()) {
            Thread.sleep(10)
        }
    }
}