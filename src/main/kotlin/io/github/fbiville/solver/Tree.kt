package io.github.fbiville.solver

import kotlin.coroutines.experimental.buildIterator

class Tree<T>(private val breadth: Int, private val depth: Int, private val root: T) {
    internal val values: MutableList<T> = MutableList(capacity(breadth, depth), { _ -> root })

    internal val capacity: Int
        get() = values.size

    fun setChildrenOf(parent: Int, vararg children: T): Tree<T> {
        checkArguments(parent, children)
        val start = breadth * parent + 1
        for (position in start until start + breadth) {
            values[position] = children[position - start]
        }
        return this
    }

    fun matchBranchByLeaf(predicate: (T) -> Boolean): List<T> {
        val match = findIndexedLeaf(predicate)

        return when (match) {
            null -> emptyList()
            else -> {
                val start = TreeDimension(match.index, depth)
                val end = TreeDimension(0, 0)
                BottomUpProgression(start, end, { parentDimension(it) })
                        .map { values[it.index] }
                        .reversed()
            }
        }
    }

    fun nonLeafIndices() = 0 until firstLeafIndex()

    operator fun get(index: Int): T = values[index]

    private fun findIndexedLeaf(filter: (T) -> Boolean): IndexedValue<T>? {
        val indexRange = firstLeafIndex() until capacity
        return values.withIndex()
                .filter { it.index in indexRange }
                .firstOrNull { filter(it.value) }
    }

    private fun parentDimension(currentDimension: TreeDimension) =
            TreeDimension((currentDimension.index - 1) / breadth, currentDimension.depth - 1)

    private fun capacity(breadth: Int, depth: Int): Int {
        return ((Math.pow(breadth.toDouble(), depth.toDouble() + 1) - 1) / (breadth - 1)).toInt()
    }

    private fun checkArguments(parent: Int, children: Array<out T>) {
        val firstLeafIndex = firstLeafIndex()
        if (parent !in 0 until firstLeafIndex) {
            throw IllegalArgumentException("Invalid parent position, got $parent, valid index between 0 and ${firstLeafIndex - 1} incl.")
        }
        if (children.size != breadth) {
            throw IllegalArgumentException("Wrong number of children, got ${children.size}, expected $breadth")
        }
    }

    private fun firstLeafIndex() = capacity - numberOfLeaves()

    private fun numberOfLeaves() = Math.pow(breadth.toDouble(), depth.toDouble()).toInt()

}

class BottomUpProgression(private val initialDimension: TreeDimension,
                          private val endDimension: TreeDimension,
                          private val progression: (TreeDimension) -> TreeDimension) : Iterable<TreeDimension> {

    override fun iterator(): Iterator<TreeDimension> {
        var dimension = initialDimension
        return buildIterator {
            while (dimension.index >= endDimension.index && dimension.depth >= endDimension.depth) {
                yield(dimension)
                dimension = progression(dimension)
            }
        }
    }
}

data class TreeDimension(val index: Int, val depth: Int)