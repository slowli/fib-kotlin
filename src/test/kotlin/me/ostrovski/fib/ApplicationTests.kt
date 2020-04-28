package me.ostrovski.fib

import com.fasterxml.jackson.databind.JsonNode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
class ApplicationTests(@Autowired val restTemplate: TestRestTemplate) {
    companion object {
        @Container
        @JvmStatic
        val redis: GenericContainer<Nothing> = GenericContainer<Nothing>("redis:5.0.9-alpine")
            .withExposedPorts(6379)

        @DynamicPropertySource
        @JvmStatic
        fun redisProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.redis.host", redis::getContainerIpAddress)
            registry.add("spring.redis.port", redis::getFirstMappedPort)
        }
    }

    @ParameterizedTest(name = "fibonacci number {0} equals {1}")
    @CsvSource("0, 1", "1, 1", "2, 2", "3, 3", "4, 5", "5, 8")
    fun `REST API is accessible`(index: Int, expected: String) {
        val entity = restTemplate.getForEntity<JsonNode>("/api/$index")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body!!.at("/value").asText()).isEqualTo(expected)
    }

    @Test
    fun `OpenAPI spec is available`() {
        val entity = restTemplate.getForEntity<JsonNode>("/v3/api-docs")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)

        val body = entity.body!!
        assertThat(body.at("/openapi").asText()).isEqualTo("3.0.1")
        assertThat(body.at("/info/title").asText()).contains("Fibonacci")
        assertThat(body.at("/info/description").asText()).contains("Fibonacci")
        assertThat(body.at("/paths").has("/api/{index}"))
        assertThat(body.at("/definitions/RenderedInteger/properties/bitLength")).isNotNull
    }

    @Test
    fun `OpenAPI spec page is the home page`() {
        val entity = restTemplate.getForEntity<String>("/")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.headers.contentType).isEqualTo(MediaType.TEXT_HTML)
    }

    @Test
    fun `GraphQL API works`() {
        val query = """{"query": "query { indexed(index: 5) { value } }"}"""
        val headers = HttpHeaders().apply { this["Content-Type"] = "application/json" }
        val request = HttpEntity(query, headers)
        val entity = restTemplate.postForEntity<JsonNode>("/graphql", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.headers.contentType!!.includes(MediaType.APPLICATION_JSON))

        val body = entity.body!!
        assertThat(body.at("/data/indexed/value").asInt()).isEqualTo(8)
    }
}
