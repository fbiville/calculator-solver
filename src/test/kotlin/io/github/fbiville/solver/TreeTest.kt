package io.github.fbiville.solver

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.Test

class TreeTest {

    @Test
    fun `initializes tree`() {
        assertThat(Tree(3, 2, "salut").capacity).isEqualTo(13)
        assertThat(Tree(2, 3, 1).capacity).isEqualTo(15)
        assertThat(Tree(2, 1, false).capacity).isEqualTo(3)
    }

    @Test
    fun `sets child values`() {
        val tree = Tree(breadth = 2, depth = 1, root = "root")

        tree.setChildrenOf(parent = 0, children = *arrayOf("first-child", "second-child"))

        assertThat(tree.values).containsExactly("root", "first-child", "second-child")
    }

    @Test
    fun `sets child values at several levels`() {
        val tree = Tree(breadth = 3, depth = 2, root = "root")

        tree.setChildrenOf(parent = 0, children = *arrayOf("1st-child", "2nd-child", "3rd-child"))
            .setChildrenOf(parent = 1, children = *arrayOf("4th-child", "5th-child", "6th-child"))
            .setChildrenOf(parent = 2, children = *arrayOf("7th-child", "8th-child", "9th-child"))
            .setChildrenOf(parent = 3, children = *arrayOf("10th-child", "11th-child", "12th-child"))

        assertThat(tree.values).containsExactly(
                "root",
                "1st-child", "2nd-child", "3rd-child",
                "4th-child", "5th-child", "6th-child",      // children of 1st-child
                "7th-child", "8th-child", "9th-child",      // children of 2nd-child
                "10th-child", "11th-child", "12th-child"    // children of 3rd-child
        )
    }

    @Test
    fun `rejects index of leaf node`() {
        val tree = Tree(breadth = 2, depth = 2, root = true)

        assertThatCode { tree.setChildrenOf(parent = 3, children = *arrayOf(true, false)) }
                .hasMessage("Invalid parent position, got 3, valid index between 0 and 2 incl.")
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `rejects negative index`() {
        val tree = Tree(breadth = 2, depth = 2, root = true)

        assertThatCode { tree.setChildrenOf(parent = -1, children = *arrayOf(true, false)) }
                .hasMessage("Invalid parent position, got -1, valid index between 0 and 2 incl.")
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `rejects child size bigger than tree breadth`() {
        val tree = Tree(breadth = 2, depth = 2, root = true)

        assertThatCode { tree.setChildrenOf(parent = 0, children = *arrayOf(true, false, true)) }
                .hasMessage("Wrong number of children, got 3, expected 2")
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `rejects child size less than tree breadth`() {
        val tree = Tree(breadth = 2, depth = 2, root = true)

        assertThatCode { tree.setChildrenOf(parent = 0, children = *arrayOf(true)) }
                .hasMessage("Wrong number of children, got 1, expected 2")
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `finds branch by leaf value`() {
        val tree = Tree(breadth = 2, depth = 2, root = 42)
                .setChildrenOf(parent = 0, children = *arrayOf(40, 84))
                .setChildrenOf(parent = 1, children = *arrayOf(38, 80))
                .setChildrenOf(parent = 2, children = *arrayOf(82, 168))

        val branch = tree.matchBranchesByLeaf({it == 82}).single()

        assertThat(branch).containsExactly(42, 84, 82)
    }

    @Test
    fun `finds branch by leaf value in k-ary tree`() {
        val tree = Tree(breadth = 3, depth = 2, root = 128)
                .setChildrenOf(parent = 0, children = *arrayOf(130, 126, 11))
                .setChildrenOf(parent = 1, children = *arrayOf(132, 128, 4))
                .setChildrenOf(parent = 2, children = *arrayOf(128, 124, 9))
                .setChildrenOf(parent = 3, children = *arrayOf(13, 9, 2))

        val branch = tree.matchBranchesByLeaf({it == 2}).single()

        assertThat(branch).containsExactly(128, 11, 2)
    }

    @Test
    fun `finds all matching branches by declaration order`() {
        val tree = Tree(breadth = 3, depth = 2, root = 128)
                .setChildrenOf(parent = 0, children = *arrayOf(130, 126, 11))
                .setChildrenOf(parent = 1, children = *arrayOf(132, 128, 4))
                .setChildrenOf(parent = 2, children = *arrayOf(128, 124, 9))
                .setChildrenOf(parent = 3, children = *arrayOf(13, 9, 2))

        val branch = tree.matchBranchesByLeaf({it == 9})

        assertThat(branch).containsExactly(
                listOf(128, 126, 9),
                listOf(128, 11, 9)
        )
    }

    @Test
    fun `returns empty branch when leaf value not found`() {
        val tree = Tree(breadth = 2, depth = 2, root = 42)
                .setChildrenOf(parent = 0, children = *arrayOf(40, 84))
                .setChildrenOf(parent = 1, children = *arrayOf(38, 80))
                .setChildrenOf(parent = 2, children = *arrayOf(82, 168))

        val branch = tree.matchBranchesByLeaf({it == 42})

        assertThat(branch).isEmpty()
    }

    @Test
    fun `computes non leaf indices`() {
        assertThat(Tree(breadth = 2, depth = 1, root = "root").nonLeafIndices()).isEqualTo(0..0)
        assertThat(Tree(breadth = 3, depth = 1, root = "root").nonLeafIndices()).isEqualTo(0..0)
        assertThat(Tree(breadth = 2, depth = 3, root = "root").nonLeafIndices()).isEqualTo(0..6)
    }
}