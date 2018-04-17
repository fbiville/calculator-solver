package io.github.fbiville.solver

fun main(args: Array<String>) {

    val problem = Solver(initialValue = 0,
            maxMoves = 5,
            targetValue = 45,
            operations = listOf(
                    NamedFunction("x9", { it.toInt() * 9 }),
                    NamedFunction("4", { Integer.parseInt(it.toString() + 4) }),
                    NamedFunction("x3", { it.toInt() * 3 }),
                    NamedFunction("3 => 5", { Integer.parseInt(it.toString().replace("3", "5")) }),
                    NamedFunction("SUM", { it.toString().chars().map { it - '0'.toInt() }.sum() })
            )
    )

    val solution = problem.solve()

    when (solution) {
        is NoSolution -> println("No solution found :(")
        is Moves -> println(solution.moves.joinToString(" -> ") { it.operation.name })
    }
}