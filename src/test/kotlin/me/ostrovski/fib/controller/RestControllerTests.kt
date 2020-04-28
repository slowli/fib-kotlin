package me.ostrovski.fib.controller

import java.math.BigInteger
import me.ostrovski.fib.logic.FibonacciService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest
@ActiveProfiles("test")
class RestControllerTests(@Autowired val mockMvc: MockMvc) {
    @MockBean
    private lateinit var fibonacciService: FibonacciService

    @Test
    fun `simple input`() {
        Mockito.`when`(fibonacciService.get(5)).thenReturn(BigInteger.valueOf(8))

        MockHttpServletRequest()

        mockMvc.perform(get("/api/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("\$.value").value("8"))
                .andExpect(jsonPath("\$.bitLength").value(4))
    }

    // since we mock `fibonacciService`, we don't actually care to *run* Redis
    @TestConfiguration
    @EnableRedisRepositories
    inner class Config
}
