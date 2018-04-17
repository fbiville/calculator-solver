package io.github.fbiville.solver

import io.github.fbiville.solver.NamedFunctions.noOp


sealed class Solution

object NoSolution : Solution()

data class SolutionBranch(val moves: List<Move>) : Solution() {

    companion object {
        fun initialMove(startValue: Number) = Move(Success(startValue, NamedFunction("INIT", {it})))

        fun apply(operation: NamedFunction, value: Computation) = Move(value.map(operation))

        fun noOp(value: Number) = Move(Success(value, noOp()))
    }
}

data class ValidatedSolution(val moves: List<SuccessfulMove>): Solution()

data class Move(val result: Computation)

data class SuccessfulMove(val result: Success)