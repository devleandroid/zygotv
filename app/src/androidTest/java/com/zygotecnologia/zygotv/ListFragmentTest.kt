package com.zygotecnologia.zygotv

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zygotecnologia.zygotv.main.MainActivity
import com.zygotecnologia.zygotv.main.MainAdapter
import com.zygotecnologia.zygotv.main.ui.ListFragment
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ListFragmentTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun infoIsShownCorrectly() {
        checkText(R.id.rv_show_list, "Zygo TV")
    }

    private fun checkText(id: Int, expectText: String) {

        Espresso.onView(withId(id)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(expectText)
            )
        )
    }

    @Test
    fun navigateFragmentDetail() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        navController.setGraph(R.navigation.nav_screen)

        val titleScenario = launchFragmentInContainer<ListFragment>()

        titleScenario.onFragment {
                fragment -> Navigation.setViewNavController(fragment.requireView(), navController)
        }

        Espresso.onView(withId(R.id.rv_show_list))
            .perform(RecyclerViewActions.scrollToPosition<MainAdapter.ViewHolder>(0))
            .perform(ViewActions.click())

        assert(navController.currentDestination?.id != R.id.detailFragment)
    }
}