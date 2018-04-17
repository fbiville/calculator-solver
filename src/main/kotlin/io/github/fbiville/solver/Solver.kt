package io.github.fbiville.solver

import io.github.fbiville.solver.SolutionBranch.Companion.initialMove

class Solver(initialValue: Number,
             maxMoves: Int,
             val targetValue: Number,
             val operations: List<NamedFunction>) {

    private val values = Tree(operations.size, maxMoves, initialMove(initialValue))

    fun solve(): Solution {
        fillTree()
        return resolve()
    }

    private fun fillTree() {
        for (index in values.nonLeafIndices()) {
            if (!targetReached(index)) {
                values.setChildrenOf(index, *computeMoves(index))
            } else {
                val noOps = operations.map { SolutionBranch.noOp(targetValue) }.toTypedArray()
                values.setChildrenOf(index, *noOps)
            }
        }
    }

    private fun resolve(): Solution {
        val possibleSolutions = values.matchBranchesByLeaf({ it.result is Success && it.result.number == targetValue })
        val bestSolution = possibleSolutions
                .map { it.drop(1) }
                .sortedByDescending { noOpCountAfterTargetReached(it) }
                .firstOrNull()

        return when (bestSolution) {
            null -> NoSolution
            else -> ValidatedSolution(bestSolution.map { SuccessfulMove(it.result as Success) }.filterNot { it.result.fn == NamedFunctions.noOp() })
        }
    }

    private fun noOpCountAfterTargetReached(it: List<Move>) =
            it.count { (it.result as Success).fn == NamedFunctions.noOp() }

    private fun targetReached(index: Int): Boolean {
        val computation = values[index].result
        return computation is Success && computation.number == targetValue
    }

    private fun computeMoves(index: Int) =
            operations.map { SolutionBranch.apply(operation = it, value = values[index].result) }.toTypedArray()

}

