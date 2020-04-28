package me.ostrovski.fib.logic

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MatrixTests {
    @Test
    fun `matrix comparison works correctly`() {
        assertThat(Matrix.identity).isEqualTo(Matrix.identity)
        assertThat(Matrix.identity * Matrix.identity).isEqualTo(Matrix.identity)
        assertThat(Matrix.base).isEqualTo(Matrix.base)
        assertThat(Matrix.base).isEqualTo(Matrix.base * Matrix.identity)
        assertThat(Matrix.base).isEqualTo(Matrix.base * Matrix.identity)
        assertThat(Matrix.identity).isNotEqualTo(Matrix.base)
    }

    @Test
    fun `power 2 of base matrix is as expected`() {
        val expected = Matrix(2, 1, 1, 1)
        assertThat(Matrix.base.square()).isEqualTo(expected)
    }
}
