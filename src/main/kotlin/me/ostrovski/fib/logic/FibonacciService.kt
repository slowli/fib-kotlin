package me.ostrovski.fib.logic

import java.math.BigInteger
import java.util.concurrent.ConcurrentHashMap
import me.ostrovski.fib.model.MatrixRecord
import me.ostrovski.fib.repository.PowersRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * Service computing Fibonacci numbers.
 *
 * # How it works
 *
 * The service uses the following identity:
 *
 * ```text
 * (f_{i + 1}, f_i) = B * (f_i, f_{i - 1})
 * ```
 *
 * where `B` is the base matrix:
 *
 * ```text
 * B = (1 1,
 *      1 0)
 * ```
 *
 * Thus, any Fibonacci number may be computed as
 *
 * ```text
 * (f_{i + 1}, _) = B^i * (f_1, f_0) = B^i * (1, 0)
 * ```
 *
 * `B^i` is then computed using [exponentiation by squaring]. `B^(2^k)` powers are stored
 * in the local cache and are periodically synced to Redis.
 *
 * [exponentiation by squaring]: https://en.wikipedia.org/wiki/Exponentiation_by_squaring
 */
@Service
class FibonacciService(powersRepository: PowersRepository) {
    private val powersCache = PowersCache(powersRepository)

    fun get(index: Int): BigInteger {
        var rest = index
        var power = 0
        var matrix = Matrix.identity

        while (rest > 0) {
            if (rest % 2 == 1) {
                matrix *= powersCache.get(power)
            }
            rest = rest shr 1
            power += 1
        }

        return matrix.outputValue
    }

    /**
     * Dumps the cache to the Redis repository.
     */
    fun dumpToRepository() = powersCache.dumpToRepository()
}

private class PowersCache(private val powersRepository: PowersRepository) {
    private val logger: Logger = LoggerFactory.getLogger(PowersCache::class.java)

    private val inner = ConcurrentHashMap<Int, Matrix>()

    init {
        inner.putAll(powersRepository.findAll().map { it.power to it.matrix })
        logger.info("Added ${inner.size} powers from Redis into local cache")
    }

    fun get(power: Int): Matrix {
        var existingPower = power
        while (existingPower >= 0 && !inner.containsKey(existingPower)) {
            existingPower -= 1
        }

        var matrix = if (existingPower >= 0) {
            inner[existingPower]!!
        } else {
            existingPower = 0
            inner[0] = Matrix.base
            Matrix.base
        }

        for (i in existingPower + 1..power) {
            logger.info("Computing power matrix for power $i")
            matrix = matrix.square()
            inner[i] = matrix
        }

        return matrix
    }

    fun dumpToRepository(): Int {
        val existingIds = mutableSetOf<Int>().apply {
            this.addAll(powersRepository.findAllById(inner.keys).map { it.power })
        }

        var count = 0
        powersRepository.saveAll(inner
            .asIterable()
            .filter { (power, _) -> !existingIds.contains(power) }
            .map { (power, matrix) -> MatrixRecord(power, matrix).also { count += 1 } })
        return count
    }
}
