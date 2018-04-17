package io.github.fbiville.solver

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SolverTest {

    @Test
    fun `solves problems with maximum 1 move`() {
        val plus2 = { n: Number -> n.toInt() + 2 }.withName("plus2")
        val times2 = { n: Number -> n.toInt() * 2 }.withName("times2")
        val problem = Solver(initialValue = 0,
                maxMoves = 1,
                targetValue = 2,
                operations = listOf(plus2, times2))

        val solution = problem.solve() as Moves

        assertThat(solution.moves)
                .containsExactly(
                        Move(operation = plus2, result = 2)
                )
    }

    @Test
    fun `solves problems with maximum 2 moves`() {
        val plus2 = { n: Number -> n.toInt() + 2 }.withName("plus2")
        val times3 = { n: Number -> n.toInt() * 3 }.withName("times3")
        val sumDigits = { n: Number -> n.toString().chars().map({it - '0'.toInt()}).sum() }.withName("sumDigits")
        val problem = Solver(initialValue = 7,
                             maxMoves = 2,
                             targetValue = 3,
                             operations = listOf(plus2, times3, sumDigits))

        val solution = problem.solve() as Moves

        assertThat(solution.moves)
                .containsExactly(
                        Move(operation = times3, result = 21),
                        Move(operation = sumDigits, result = 3))
    }

    @Test
    fun `solves problems with maximum n moves`() {
        val plus2 = { n: Number -> n.toInt() + 2 }.withName("plus2")
        val times3 = { n: Number -> n.toInt() * 3 }.withName("times3")
        val sumDigits = { n: Number -> n.toString().chars().map({it - '0'.toInt()}).sum() }.withName("sumDigits")
        val problem = Solver(initialValue = 2817,
                             maxMoves = 4,
                             targetValue = 168,
                             operations = listOf(plus2, times3, sumDigits))

        val solution = problem.solve() as Moves

        assertThat(solution.moves)
                .containsExactly(
                        Move(operation = sumDigits, result = 18),
                        Move(operation = times3, result = 54),
                        Move(operation = plus2, result = 56),
                        Move(operation = times3, result = 168))
    }


}

private fun <I, O> Function1<I, O>.withName(name: String): NamedFunction<I, O> = NamedFunction(name, this)
