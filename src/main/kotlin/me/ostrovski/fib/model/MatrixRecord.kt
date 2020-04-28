package me.ostrovski.fib.model

import me.ostrovski.fib.logic.Matrix
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

/**
 * Data model for Redis, associating a power matrix with the power.
 */
@RedisHash("fib_powers")
class MatrixRecord(
    /**
     * The power of 2 the associated matrix corresponds to. For example, 0 corresponds to
     * `2^0 = 1` (i.e., the associated [matrix] is [Matrix.base]).
     */
    @Id val power: Int,
    /**
     * Power matrix.
     */
    val matrix: Matrix
)
