package me.ostrovski.fib.logic

import java.math.BigInteger

/**
 * 2x2 matrix with [BigInteger]s.
 */
data class Matrix(private val elements: Array<BigInteger>) {
    val outputValue: BigInteger get() = elements[0]

    /**
     * Creates matrix based on provided elements.
     */
    constructor(a: Int, b: Int, c: Int, d: Int) :
        this(arrayOf(
                BigInteger.valueOf(a.toLong()),
                BigInteger.valueOf(b.toLong()),
                BigInteger.valueOf(c.toLong()),
                BigInteger.valueOf(d.toLong())
        ))

    /**
     * Multiplies this matrix by another one.
     */
    operator fun times(other: Matrix): Matrix {
        return Matrix(arrayOf(
                elements[0] * other.elements[0] + elements[1] * other.elements[2],
                elements[0] * other.elements[1] + elements[1] * other.elements[3],
                elements[2] * other.elements[0] + elements[3] * other.elements[2],
                elements[2] * other.elements[1] + elements[3] * other.elements[3]
        ))
    }

    /**
     * Shortcut for multiplying a matrix by itself.
     */
    fun square() = times(this)

    override fun equals(other: Any?) = when (other) {
        is Matrix -> elements.contentEquals(other.elements)
        else -> false
    }

    override fun hashCode() = elements.contentHashCode()

    companion object {
        val identity =
            Matrix(arrayOf(BigInteger.ONE, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ONE))
        val base = Matrix(arrayOf(BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ZERO))
    }
}
