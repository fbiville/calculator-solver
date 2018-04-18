package io.github.fbiville.solver

interface Visitor<in T> {
    fun visit(element: T): String
}

class TreeVisitor<T>(private val elementVisitor: Visitor<Pair<T, T>>) : Visitor<Tree<T>> {

    override fun visit(element: Tree<T>): String {
        return """
            |strict digraph {
            |${visitElements(element)}
            |}
        """.trimMargin("|")
    }

    private fun visitElements(tree: Tree<T>): String {
        val relations = zipElements(tree, tree.rootElement())
                .toList()
                .flatMap { pair -> pair.second.map { Pair(pair.first, it) } }
        return "\t" + relations.joinToString("\n\t") { elementVisitor.visit(it) }
    }

    private fun zipElements(tree: Tree<T>, root: Node<T>): Map<T, List<T>> {
        val map = LinkedHashMap<T, List<T>>()
        val children = tree.getChildrenOf(root.index)
        map[root.value] = children.map { it.value }
        children.forEach { child -> map.putAll(zipElements(tree, child)) }
        return map
    }
}