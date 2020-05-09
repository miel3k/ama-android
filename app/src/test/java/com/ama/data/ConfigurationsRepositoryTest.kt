package com.ama.data

import com.ama.data.configurations.ConfigurationsDataSource
import com.ama.data.configurations.ConfigurationsRepository
import com.ama.data.configurations.model.Configuration
import com.ama.utils.CoroutinesTestRule
import com.ama.utils.any
import com.ama.utils.eq
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations
import java.io.IOException

class ConfigurationsRepositoryTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var remote: ConfigurationsDataSource

    @Mock
    private lateinit var local: ConfigurationsDataSource

    private lateinit var repository: ConfigurationsDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = ConfigurationsRepository(remote, local)
    }

    @Test
    fun `getConfiguration should call getConfiguration on local repository`() {
        /* Given */
        val configurationId = "configurationId"
        runBlocking {
            /* When */
            repository.getConfiguration(configurationId)
            /* Then */
            Mockito.verify(local, times(1))
                .getConfiguration(eq(configurationId))
        }
    }

    @Test
    fun `saveConfiguration should call saveConfiguration on local repository`() {
        /* Given */
        val configurationId = "configurationId"
        val configuration = Configuration(configurationId)
        runBlocking {
            /* When */
            repository.saveConfiguration(configuration)
            /* Then */
            Mockito.verify(local, times(1))
                .saveConfiguration(eq(configuration))
        }
    }

    @Test
    fun `loadConfiguration should call saveConfiguration on local repository when remote result success`() {
        /* Given */
        val configurationId = "configurationId"
        val configuration = Configuration(configurationId)
        runBlocking {
            /* When */
            `when`(remote.loadConfiguration(eq(configurationId)))
                .thenReturn(RepositoryResult.Success(configuration))
            repository.loadConfiguration(configurationId)
            /* Then */
            Mockito.verify(local, times(1))
                .saveConfiguration(eq(configuration))
        }
    }

    @Test
    fun `loadConfiguration should not call saveConfiguration on local repository when remote result error`() {
        /* Given */
        val configurationId = "configurationId"
        runBlocking {
            /* When */
            `when`(remote.loadConfiguration(eq(configurationId)))
                .thenReturn(RepositoryResult.Error(IOException()))
            repository.loadConfiguration(configurationId)
            /* Then */
            Mockito.verify(local, times(0))
                .saveConfiguration(any())
        }
    }
}