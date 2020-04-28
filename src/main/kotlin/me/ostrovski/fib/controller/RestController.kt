package me.ostrovski.fib.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import me.ostrovski.fib.logic.FibonacciService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller of the application.
 */
@RestController
@RequestMapping("/api", produces = ["application/json"])
@Tag(
    name = "REST controller",
    description = "Provides REST interface to compute Fibonacci numbers"
)
class RestController(private val fibonacciService: FibonacciService) {
    @GetMapping("/{index}")
    @Operation(
        summary = "Gets a single Fibonacci number by a zero-based index",
        responses = [
            ApiResponse(responseCode = "200", description = "Computed Fibonacci number")
        ]
    )
    fun byIndex(
        @PathVariable("index")
        @Parameter(description = "Zero-based index in the Fibonacci sequence", example = "5")
        index: Int
    ) = fibonacciService.get(index).render()
}
