package me.ostrovski.fib.tasks

import me.ostrovski.fib.model.MatrixRecord
import me.ostrovski.fib.repository.PowersRepository
import org.awaitility.Awaitility
import org.awaitility.Duration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class CacheSyncTaskTests {
    @SpyBean
    lateinit var task: CacheSyncTask

    // Mock persistence to be able to perform tests without Redis on.
    @MockBean
    private lateinit var powersRepository: PowersRepository

    @BeforeEach
    fun setup() {
        Mockito.`when`(powersRepository.findAll()).thenReturn(emptyList<MatrixRecord>())
    }

    @Test
    fun `syncing is timely executed`() {
        Awaitility.await().atMost(Duration.FIVE_SECONDS).untilAsserted {
            Mockito.verify(task, Mockito.atLeast(2)).syncCache()
        }
    }
}
