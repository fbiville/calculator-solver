package io.github.fbiville.solver

sealed class Computation {
    abstract fun map(fn: NamedFunction): Computation
}

object Error : Computation() {
    override fun map(fn: NamedFunction): Computation = Error
}

data class Success(val number: Number, val fn: NamedFunction) : Computation() {
    override fun map(fn: NamedFunction): Computation {
        val result = fn.invoke(number)
        return if (result == null) Error
        else Success(result, fn)
    }
}