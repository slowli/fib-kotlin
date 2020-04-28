package me.ostrovski.fib.logic

import me.ostrovski.fib.model.MatrixRecord
import me.ostrovski.fib.repository.PowersRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.ArgumentMatchers.argThat
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class FibonacciServiceTests(@Autowired val service: FibonacciService) {
    // Mock persistence to be able to perform tests without Redis on.
    @MockBean
    private lateinit var powersRepository: PowersRepository

    @BeforeEach
    fun setup() {
        Mockito.`when`(powersRepository.findAll()).thenReturn(emptyList<MatrixRecord>())
    }

    @ParameterizedTest(name = "fibonacci number {0} equals {1}")
    @CsvSource("0, 1", "1, 1", "2, 2", "3, 3", "4, 5", "5, 8")
    fun `computing small value`(index: Int, expectedValue: Int) {
        assertThat(service.get(index)).isEqualTo(expectedValue)
    }

    @Test
    fun `computing large value`() {
        assertThat(service.get(1000).bitLength()).isGreaterThan(100)
    }

    @Test
    fun `dumping power matrices`() {
        service.get(1000) // Call with a large value to ensure that we have info to dump
        service.dumpToRepository()

        Mockito
            .verify(powersRepository, Mockito.times(1))
            .saveAll(argThat { it.count() >= 10 })
    }
}
