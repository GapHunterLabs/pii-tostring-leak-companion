package dev.gaphunter.piitostringleakcompanion.detect

import com.intellij.psi.PsiMethodCallExpression

/**
 * Text signals this plugin uses to recognize a logging/print call --
 * a known SLF4J/Log4j-style method name (`info`/`warn`/`error`/
 * `debug`/`trace`) called on a variable conventionally named `log`/
 * `logger`, or `System.out`/`System.err`'s `println`. Deliberately a
 * closed, name-based check (not a resolved type), same "match a known
 * name, don't resolve a symbol" discipline used elsewhere in this
 * catalog.
 */
object LoggingCallSignals {

    private val LOG_METHOD_NAMES = setOf("info", "warn", "error", "debug", "trace")
    private val LOG_VARIABLE_NAMES = setOf("log", "logger")

    fun isLoggingCall(call: PsiMethodCallExpression): Boolean {
        val methodName = call.methodExpression.referenceName ?: return false
        if (methodName !in LOG_METHOD_NAMES) return false
        val qualifierText = call.methodExpression.qualifierExpression?.text?.lowercase() ?: return false
        return qualifierText in LOG_VARIABLE_NAMES
    }

    fun isPrintlnCall(call: PsiMethodCallExpression): Boolean {
        val methodName = call.methodExpression.referenceName ?: return false
        if (methodName != "println") return false
        val qualifierText = call.methodExpression.qualifierExpression?.text ?: return false
        return qualifierText == "System.out" || qualifierText == "System.err"
    }
}
