package com.rbrauwers.newsapp.source

import android.app.Application
import android.content.pm.ActivityInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import app.cash.turbine.test
import com.rbrauwers.newsapp.tests.FakeSourceRepository
import com.rbrauwers.newsapp.tests.sourcesList
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class SourcesViewModelTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @AndroidEntryPoint
    internal class TestActivity : AppCompatActivity() {
        val sourcesViewModel: SourcesViewModel by viewModels()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun init() {
        val appContext: Application = ApplicationProvider.getApplicationContext()
        val activityInfo = ActivityInfo().apply {
            name = TestActivity::class.java.name
            packageName = appContext.packageName
            theme = androidx.appcompat.R.style.Theme_AppCompat
        }
        shadowOf(appContext.packageManager).addOrUpdateActivity(activityInfo)

        hiltRule.inject()
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Automatically injecting fake classes with Hilt does not fit well
     * with flow test, due the impossibility to control data emission.
     */
    @Test
    fun testSourcesFlowFirstAttempt() {
        launchActivity<TestActivity>().use {
            it.onActivity { activity ->
                assert(activity.sourcesViewModel.hashCode() > 0)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testSourcesFlow() = runTest {
        val repository = FakeSourceRepository()
        val viewModel = SourcesViewModel(repository)

        // Create an empty collector for the StateFlow
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.sourcesUiState.collect()
        }

        viewModel.sourcesUiState.test {
            Assert.assertEquals(SourcesUiState.Loading, awaitItem())

            repository.emitOne()
            advanceUntilIdle()
            Assert.assertEquals(1, (awaitItem() as? SourcesUiState.Success)?.sources?.size)

            repository.sync()
            advanceUntilIdle()
            Assert.assertEquals(sourcesList.size, (awaitItem() as? SourcesUiState.Success)?.sources?.size)

            job.cancel()
        }
    }

}