package io.falu.android.models.evaluations

import io.falu.android.models.FaluModel
import kotlinx.parcelize.Parcelize
import java.io.File

/**
 * [The evaluation request object](https://falu.io)
 */
@Parcelize
data class EvaluationRequest(
    /**
     * Represents the currency e.g kes
     */
    var currency: String = "kes",

    /**
     * Represents the scope within which an evaluation is generated.
     * This can also be considered the purpose of the evaluation.
     */
    var scope: String,

    /**
     * Represents the kind of provider used for a statement in an evaluation.
     *
     */
    var provider: String,

    /**
     * The full name of the person or business that owns the statement.
     *
     */
    var name: String,

    /**
     * The Phone number for attached to the statement.
     * Only required for statements generated against a phone number e.g. mpesa
     *
     */
    var phone: String?,

    /**
     * Password to open the uploaded file. Only required for password protected files.
     * Certain providers only provide password protected files.
     * In such cases the password should always be provided.
     */
    var password: String?,

    /**
     * Unique identifier of the file containing the statement.
     */
    var file: String
) : FaluModel()