package dev.gaphunter.piitostringleakcompanion.detect

/**
 * Field-name fragments this plugin treats as "looks like it holds PII"
 * -- same calibrated lexicon as `pii-field-annotation-companion`'s own
 * `PiiFieldSignals` (this catalog), kept as a separate standalone copy
 * so each plugin stays independently installable. Deliberately broad
 * substring matches (not resolved calls), only the most common,
 * unambiguous PII categories -- not an exhaustive privacy-law
 * taxonomy.
 */
object PiiFieldSignals {

    private val PII_NAME_FRAGMENTS = listOf(
        "email",
        "ssn",
        "socialsecurity",
        "phonenumber",
        "phone_number",
        "dateofbirth",
        "date_of_birth",
        "creditcard",
        "credit_card",
        "passportnumber",
        "passport_number",
        "nationalid",
        "national_id",
        "taxid",
        "tax_id",
    )

    /** Bare, short field names that need an exact match, not substring -- avoids matching e.g. "dobsonUsername". */
    private val PII_EXACT_NAMES = setOf("dob", "phone", "ssn")

    fun looksLikePii(fieldName: String): Boolean {
        val lower = fieldName.lowercase()
        if (lower in PII_EXACT_NAMES) return true
        return PII_NAME_FRAGMENTS.any { lower.contains(it) }
    }
}
