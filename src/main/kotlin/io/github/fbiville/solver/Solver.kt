package io.github.fbiville.solver

import io.github.fbiville.solver.Move.Companion.initialMove
import io.github.fbiville.solver.NamedFunction.Companion.identity

class Solver(initialValue: Number,
             maxMoves: Int,
             val targetValue: Number,
             val operations: List<NamedFunction<Number, Number>>) {

    private val values = Tree(operations.size, maxMoves, initialMove(initialValue))

    fun solve(): Solution {
        fillTree()
        return resolve()
    }

    private fun fillTree() {
        for (index in values.nonLeafIndices()) {
            values.setChildrenOf(index, *computeMoves(index))
        }
    }

    private fun resolve(): Solution {
        val branch = values.matchBranchByLeaf({ it.result == targetValue })
        return when {
            branch.isEmpty() -> NoSolution
            else -> Moves(branch.drop(1))
        }
    }

    private fun computeMoves(index: Int) =
            operations.map { Move.apply(operation = it, value = values[index].result) }.toTypedArray()

}

sealed class Solution
object NoSolution: Solution()
data class Moves(val moves: List<Move>) : Solution()

data class Move(val operation: NamedFunction<Number, Number>, val result: Number) {
    companion object {
        fun initialMove(startValue: Number) = Move(identity(), startValue)
        fun apply(operation: NamedFunction<Number, Number>, value: Number) = Move(operation, operation(value))
    }
}

data class NamedFunction<in I, out O>(val name: String,
                                      private val function: Function1<I, O>): Function1<I, O> {

    override operator fun invoke(input: I): O = function.invoke(input)

    override fun equals(other: Any?): Boolean {
        val function = other as? NamedFunction<*, *>
        return when (function) {
            null -> false
            else -> function.name == this.name
        }

    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    companion object {
        fun <I> identity() = NamedFunction<I, I>("identity", {it})
    }
}