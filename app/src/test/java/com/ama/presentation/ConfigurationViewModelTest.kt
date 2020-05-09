package com.ama.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ama.data.RepositoryResult
import com.ama.data.configurations.ConfigurationsDataSource
import com.ama.data.configurations.model.Configuration
import com.ama.data.events.EventsDataSource
import com.ama.presentation.configuration.viewmodel.ConfigurationViewModel
import com.ama.utils.CoroutinesTestRule
import com.ama.utils.any
import com.ama.utils.eq
import com.ama.utils.testObserver
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.io.IOException

class ConfigurationViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var configurationsRepository: ConfigurationsDataSource

    @Mock
    private lateinit var eventsRepository: EventsDataSource

    @InjectMocks
    private lateinit var viewModel: ConfigurationViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `isLoading should post false value on init`() {
        /* Given */
        val expectedIsLoadingStates = listOf(false)
        /* When */
        val isLoadingObserver = viewModel.isLoading.testObserver()
        /* Then */
        assertEquals(expectedIsLoadingStates, isLoadingObserver.observedValues)
    }

    @Test
    fun `error should post ConfigurationIdEmpty value when configurationId is empty`() {
        /* Given */
        val expectedIsLoadingStates = listOf(false)
        val expectedErrors = listOf(ConfigurationViewModel.Error.ConfigurationIdEmpty)
        /* When */
        val isLoadingObserver = viewModel.isLoading.testObserver()
        val errorObserver = viewModel.error.testObserver()
        viewModel.loadConfiguration("")
        /* Then */
        assertEquals(expectedIsLoadingStates, isLoadingObserver.observedValues)
        assertEquals(expectedErrors, errorObserver.observedValues)
    }

    @Test
    fun `success should post configuration id value when loadConfiguration result success`() {
        /* Given */
        val configurationId = "configurationId"
        val configuration = Configuration(configurationId)
        val expectedIsLoadingStates = listOf(false, true, false)
        runBlocking {
            /* When */
            `when`(configurationsRepository.loadConfiguration(eq(configurationId)))
                .thenReturn(RepositoryResult.Success(configuration))
            val isLoadingObserver = viewModel.isLoading.testObserver()
            val successObserver = viewModel.success.testObserver()
            viewModel.loadConfiguration(configurationId)
            /* Then */
            assertEquals(expectedIsLoadingStates, isLoadingObserver.observedValues)
            assertEquals(1, successObserver.observedValues.size)
            assertEquals(configurationId, successObserver.observedValues[0])
            verify(eventsRepository, times(1)).saveEvent(any())
        }
    }

    @Test
    fun `error should post Remote value when loadConfiguration result error`() {
        /* Given */
        val configurationId = "configurationId"
        val expectedIsLoadingStates = listOf(false, true, false)
        runBlocking {
            /* When */
            `when`(configurationsRepository.loadConfiguration(eq(configurationId)))
                .thenReturn(RepositoryResult.Error(IOException()))
            val isLoadingObserver = viewModel.isLoading.testObserver()
            val errorObserver = viewModel.error.testObserver()
            viewModel.loadConfiguration(configurationId)
            /* Then */
            assertEquals(expectedIsLoadingStates, isLoadingObserver.observedValues)
            assertEquals(1, errorObserver.observedValues.size)
            assertTrue(errorObserver.observedValues[0] is ConfigurationViewModel.Error.Remote)
            verify(eventsRepository, times(1)).saveEvent(any())
        }
    }

    @Test
    fun `savePermissionEvent should create event and call repository method when isGranted true`() {
        /* Given */
        val isGranted = true
        runBlocking {
            /* When */
            viewModel.savePermissionEvent(isGranted)
            /* Then */
            verify(eventsRepository, times(1)).saveEvent(any())
        }
    }

    @Test
    fun `savePermissionEvent should create event and call repository method when isGranted false`() {
        /* Given */
        val isGranted = false
        runBlocking {
            /* When */
            viewModel.savePermissionEvent(isGranted)
            /* Then */
            verify(eventsRepository, times(1)).saveEvent(any())
        }
    }
}
