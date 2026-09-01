package dev.gaphunter.piitostringleakcompanion.model

import com.intellij.psi.PsiElement

/** One logging/print call passed an instance of a class whose `toString()` exposes a PII field, with no explicit field selection. */
data class PiiToStringHit(val anchor: PsiElement, val className: String, val piiFieldName: String)
