package io.github.fbiville.solver

data class NamedFunction(val name: String,
                         private val function: (Number) -> Number?) : Function1<Number, Number?> {

    override operator fun invoke(input: Number): Number? = try {
        function.invoke(input)
    } catch (e: RuntimeException) {
        null
    }

    override fun equals(other: Any?): Boolean {
        val function = other as? NamedFunction
        return when (function) {
            null -> false
            else -> function.name == this.name
        }

    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

}

