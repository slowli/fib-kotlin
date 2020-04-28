package me.ostrovski.fib.controller.graphql

import graphql.kickstart.tools.GraphQLQueryResolver
import me.ostrovski.fib.controller.render
import me.ostrovski.fib.logic.FibonacciService
import org.springframework.stereotype.Component

@Component
class Query(private val fibonacciService: FibonacciService) : GraphQLQueryResolver {
    fun indexed(index: Int) = fibonacciService.get(index).render()
}
