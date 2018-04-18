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

class MoveVisitor : Visitor<Pair<Move, Move>> {

    override fun visit(element: Pair<Move, Move>): String {
        return "${serialize(element.first)} -> ${serialize(element.second)}"
    }

    private fun serialize(move: Move): String {
        val result = move.result
        return when (result) {
            is Success -> "${result.fn.name}_result_is_${result.number}"
            else -> "ERROR"
        }
    }
}