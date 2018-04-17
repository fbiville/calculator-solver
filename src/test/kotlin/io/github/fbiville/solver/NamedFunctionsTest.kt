package io.github.fbiville.solver

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class NamedFunctionsTest {

    @Test
    fun `no-op does nothing`() {
        assertThat(NamedFunctions.noOp().invoke(42)).isEqualTo(42L)
    }

    @Test
    fun `substitutes number with other number`() {
        assertThat(NamedFunctions.substitute(51, 42).invoke(1515)).isEqualTo(1425L)
    }

    @Test
    fun `reverses number`() {
        assertThat(NamedFunctions.reverse().invoke(1234)).isEqualTo(4321L)
    }

    @Test
    fun `adds to number`() {
        assertThat(NamedFunctions.plus(2).invoke(8)).isEqualTo(10L)
    }

    @Test
    fun `substracts to number`() {
        assertThat(NamedFunctions.minus(2).invoke(12)).isEqualTo(10L)
    }

    @Test
    fun `concats to number`() {
        assertThat(NamedFunctions.concat(0).invoke(1)).isEqualTo(10L)
    }

    @Test
    fun `shifts digits to right`() {
        assertThat(NamedFunctions.shiftRight().invoke(123)).isEqualTo(312L)
    }

    @Test
    fun `shifts digits to left`() {
        assertThat(NamedFunctions.shiftLeft().invoke(123)).isEqualTo(231L)
    }

    @Test
    fun `sums digits`() {
        assertThat(NamedFunctions.sumDigits().invoke(123)).isEqualTo(6L)
    }

    @Test
    fun `mirrors number`() {
        assertThat(NamedFunctions.mirror().invoke(123)).isEqualTo(123321L)
    }

    @Test
    fun `powers number`() {
        assertThat(NamedFunctions.pow(3).invoke(10)).isEqualTo(1000L)
    }

    @Test
    fun `powers number returns null when overflowing`() {
        assertThat(NamedFunctions.pow(3).invoke(10000000000000000L)).isNull()
    }
}