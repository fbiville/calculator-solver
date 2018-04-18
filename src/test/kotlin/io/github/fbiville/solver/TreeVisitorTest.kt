package io.github.fbiville.solver

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TreeVisitorTest {

    @Test
    fun `generates graphviz graph`() {
        val tree = Tree(24, 2, 2)
                .setChildrenOf(0, 48, 72)
                .setChildrenOf(1, 96, 144)
                .setChildrenOf(2, 144, 216)

        val result: String = TreeVisitor(IntPairVisitor()).visit(tree)

        assertThat(result).isEqualToNormalizingWhitespace("""
            |strict digraph {
            |   24 -> 48
            |   24 -> 72
            |   48 -> 96
            |   48 -> 144
            |   72 -> 144
            |   72 -> 216
            |}""".trimMargin("|"))
    }
}

class IntPairVisitor : Visitor<Pair<Int, Int>> {

    override fun visit(element: Pair<Int, Int>): String {
        return "${element.first} -> ${element.second}"
    }
}