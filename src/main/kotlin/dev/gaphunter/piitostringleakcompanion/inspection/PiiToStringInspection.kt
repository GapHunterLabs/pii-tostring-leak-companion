package dev.gaphunter.piitostringleakcompanion.inspection

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import dev.gaphunter.piitostringleakcompanion.detect.JavaPiiToStringFinder
import dev.gaphunter.piitostringleakcompanion.model.PiiToStringHit
import dev.gaphunter.piitostringleakcompanion.review.ReviewPrompt

/**
 * Flags a logging/print call passed an instance of a class whose
 * `toString()` exposes a PII field -- the field never appears
 * textually at the call site, it leaks implicitly through the
 * object's own serialization. Runs via `checkFile` (same shape as
 * every other inspection in this catalog); [JavaPiiToStringFinder]
 * does the real PSI walk.
 */
class PiiToStringInspection : LocalInspectionTool() {

    companion object {
        const val MAX_FILE_LENGTH = 500_000
    }

    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor>? {
        if (file.text.length > MAX_FILE_LENGTH) return null
        if (file !is PsiJavaFile) return null

        val hits = JavaPiiToStringFinder.findAll(file)
        if (hits.isEmpty()) return null

        val problems = hits.map { hit ->
            manager.createProblemDescriptor(
                hit.anchor,
                messageFor(hit),
                isOnTheFly,
                emptyArray(),
                ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
            )
        }

        val path = file.virtualFile?.path
        if (path != null) {
            for (hit in hits) {
                val lineNumber = file.viewProvider.document?.getLineNumber(hit.anchor.textRange.startOffset) ?: -1
                ReviewPrompt.recordHit(file.project, "$path:$lineNumber")
            }
        }

        return problems.toTypedArray()
    }

    private fun messageFor(hit: PiiToStringHit): String =
        "Logging a ${hit.className} instance whose toString() exposes '${hit.piiFieldName}' -- the PII field " +
            "never appears at this call site, it leaks implicitly through the object's own serialization"
}
