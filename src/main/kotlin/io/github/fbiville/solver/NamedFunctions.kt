package io.github.fbiville.solver

import java.lang.Long.parseLong

object NamedFunctions {

    fun noOp() = NamedFunction("NO-OP", { it.toLong() })

    fun substitute(search: Long, replace: Long): NamedFunction {
        return NamedFunction("$search=>$replace", {
            val substitution = it.toString().replace(search.toString(10), replace.toString(10))
            parseLong(substitution, 10)
        })
    }

    fun reverse() = NamedFunction("Reverse", { parseLong(it.toString().reversed(), 10) })

    fun plus(number: Long) = NamedFunction("+$number", { it.toLong() + number })

    fun minus(number: Long) = NamedFunction("-$number", { it.toLong() - number })

    fun concat(number: Long) = NamedFunction("$number", { parseLong(it.toString() + number, 10) })

    fun shiftRight() = NamedFunction("Shift>", { shiftRight(it) })

    fun shiftLeft() = NamedFunction("<Shift", { shiftLeft(it) })

    fun sumDigits() = NamedFunction("SUM", { it.toString().chars().map { it - '0'.toInt() }.sum().toLong() })

    fun mirror() = NamedFunction("Mirror", { parseLong(it.toString() + it.toString().reversed(), 10) })

    fun pow(power: Int) = NamedFunction("Mirror", { base ->
        (1L..power).map { base.toLong() }.reduce {n1, n2 -> Math.multiplyExact(n1, n2)}
    })

    private fun shiftRight(it: Number): Number? {
        val chars: CharArray = it.toString().toCharArray()
        val result = CharArray(chars.size, { chars.last() })
        for (i in 0 until chars.size - 1) {
            result[i + 1] = chars[i]
        }
        return parseLong(String(result), 10)
    }

    private fun shiftLeft(it: Number): Number? {
        val chars: CharArray = it.toString().toCharArray()
        val result = CharArray(chars.size, { chars[0] })
        for (i in 1 until chars.size) {
            result[i - 1] = chars[i]
        }
        return parseLong(String(result), 10)
    }
}