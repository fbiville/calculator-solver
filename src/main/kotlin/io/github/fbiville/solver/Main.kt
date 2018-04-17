package io.github.fbiville.solver

import io.github.fbiville.solver.NamedFunctions.reverse
import io.github.fbiville.solver.NamedFunctions.shiftRight
import io.github.fbiville.solver.NamedFunctions.substitute

fun main(args: Array<String>) {

    val problem = Solver(
            initialValue = 2152L,
            maxMoves = 6,
            targetValue = 13L,
            operations = listOf(
                    substitute(25, 12),
                    substitute(21, 3),
                    substitute(12, 5),
                    shiftRight(),
                    reverse()))

    val solution = problem.solve()

    when (solution) {
        is ValidatedSolution -> println(solution.moves.map { it.result.fn.name })
        else -> println("No solution found :(")
    }
}


