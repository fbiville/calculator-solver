package io.github.fbiville.solver

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.lang.Long.parseLong

class ComputationTest {

    @Test
    fun `maps one successful computation to another`() {
        val initialComputation = Success(9999L, NamedFunction("identity", { it }))
        val fn = NamedFunction("div9", { it.toLong() / 9 })

        val result = initialComputation.map(fn)

        assertThat(result).isEqualTo(Success(1111L, fn))
    }

    @Test
    fun `maps error to error`() {
        val fn = NamedFunction("div9", { it.toLong() / 9 })

        val result = Error.map(fn)

        assertThat(result).isEqualTo(Error)
    }

    @Test
    fun `gracefully rejects out of range computations`() {
        val initialComputation = Success(999999999999999999L, NamedFunction("identity", { it }))
        val fn = NamedFunction("pow3", { parseLong(Math.pow(it.toDouble(), 3.0).toString()) })

        val result = initialComputation.map(fn)

        assertThat(result).isEqualTo(Error)

    }
}