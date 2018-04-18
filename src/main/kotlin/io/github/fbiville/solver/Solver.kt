package io.github.fbiville.solver

import io.github.fbiville.solver.SolutionBranch.Companion.initialMove
import java.nio.file.Files

class Solver(initialValue: Number,
             maxMoves: Int,
             val targetValue: Number,
             val operations: List<NamedFunction>) {

    private val values = Tree(initialMove(initialValue), operations.size, maxMoves)

    fun solve(): Solution {
        fillTree()
        debug()
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

    private fun debug() {
        val tempFile = Files.createTempFile("tree", ".dot")
        println(tempFile.toFile().absolutePath)
        val graphviz = TreeVisitor(MoveVisitor()).visit(values)
        Files.write(tempFile, graphviz.toByteArray(Charsets.UTF_8))
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

