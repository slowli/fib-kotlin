package me.ostrovski.fib.controller

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigInteger

@Schema(description = "Big integer with additional information")
data class RenderedInteger(
    @get: Schema(description = "Decimal value of the integer", example = "8")
    val value: String,

    @get: Schema(description = "Number of bits in the value", example = "4")
    val bitLength: Int
) {
    constructor(integer: BigInteger) :
        this(value = integer.toString(), bitLength = integer.bitLength())
}

internal fun BigInteger.render() = RenderedInteger(this)
