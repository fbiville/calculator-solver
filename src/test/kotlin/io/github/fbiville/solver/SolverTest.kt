package io.github.fbiville.solver

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SolverTest {

    @Test
    fun `solves problems with maximum 1 move`() {
        val plus2 = { n: Number -> (n.toInt() + 2) }.toNamedFunction("plus2")
        val times2 = { n: Number -> (n.toInt() * 2) }.toNamedFunction("times2")
        val problem = Solver(initialValue = 0,
                maxMoves = 1,
                targetValue = 2,
                operations = listOf(plus2, times2))

        val solution = problem.solve() as ValidatedSolution

        assertThat(solution.moves)
                .containsExactly(
                        SuccessfulMove(Success(2, plus2)))
    }

    @Test
    fun `solves problems with maximum 2 moves`() {
        val plus2 = { n: Number -> (n.toInt() + 2) }.toNamedFunction("plus2")
        val times3 = { n: Number -> (n.toInt() * 3) }.toNamedFunction("times3")
        val sumDigits = { n: Number -> n.toString().chars().map({ it - '0'.toInt() }).sum() }.toNamedFunction("sumDigits")
        val problem = Solver(
                initialValue = 7,
                maxMoves = 2,
                targetValue = 3,
                operations = listOf(plus2, times3, sumDigits))

        val solution = problem.solve() as ValidatedSolution

        assertThat(solution.moves)
                .containsExactly(
                        SuccessfulMove(Success(21, times3)),
                        SuccessfulMove(Success(3, sumDigits)))
    }

    @Test
    fun `solves problems with maximum n moves`() {
        val plus2 = { n: Number -> (n.toInt() + 2) }.toNamedFunction("plus2")
        val times3 = { n: Number -> (n.toInt() * 3) }.toNamedFunction("times3")
        val sumDigits = { n: Number -> n.toString().chars().map({ it - '0'.toInt() }).sum() }.toNamedFunction("sumDigits")
        val problem = Solver(initialValue = 2817,
                maxMoves = 4,
                targetValue = 168,
                operations = listOf(plus2, times3, sumDigits))

        val solution = problem.solve() as ValidatedSolution

        assertThat(solution.moves)
                .containsExactly(
                        SuccessfulMove(Success(18, sumDigits)),
                        SuccessfulMove(Success(54, times3)),
                        SuccessfulMove(Success(56, plus2)),
                        SuccessfulMove(Success(168, times3)))
    }


    @Test
    fun `selects the shortest solution`() {
        val times3 = { n: Number -> (n.toInt() * 3) }.toNamedFunction("times3")
        val times9 = { n: Number -> (n.toInt() * 9) }.toNamedFunction("times9")
        val problem = Solver(initialValue = 3,
                maxMoves = 2,
                targetValue = 27,
                operations = listOf(times3, times9))
        
        val solution = problem.solve() as ValidatedSolution

        assertThat(solution.moves).containsExactly(SuccessfulMove(Success(27, times9)))
    }
}

private fun Function1<Number, Number>.toNamedFunction(name: String): NamedFunction = NamedFunction(name, this)
