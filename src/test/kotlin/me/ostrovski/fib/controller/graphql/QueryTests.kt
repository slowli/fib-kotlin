package me.ostrovski.fib.controller.graphql

import com.graphql.spring.boot.test.GraphQLTest
import com.graphql.spring.boot.test.GraphQLTestTemplate
import java.math.BigInteger
import me.ostrovski.fib.logic.FibonacciService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus

@GraphQLTest
class QueryTests(@Autowired private val graphql: GraphQLTestTemplate) {
    @MockBean
    private lateinit var fibonacciService: FibonacciService

    @Test
    fun `get Fibonacci number`() {
        Mockito.`when`(fibonacciService.get(5)).thenReturn(BigInteger.valueOf(8))
        val response = graphql.postForResource("graphql/indexed.graphql")
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response["\$.data.indexed.value"]).isEqualTo("8")
        assertThat(response["\$.data.indexed.bitLength"]).isEqualTo("4")
    }

    @Test
    fun `get Fibonacci number with only value`() {
        Mockito.`when`(fibonacciService.get(10)).thenReturn(BigInteger.valueOf(89))
        val response = graphql.postForResource("graphql/indexed-value.graphql")
        assertThat(response.isOk)
        val responseMap = response.get("\$.data.indexed", HashMap::class.java)
        assertThat(responseMap["value"]).isEqualTo("89")
        assertThat(!responseMap.containsKey("bitLength"))
    }
}
