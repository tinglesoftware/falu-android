package io.falu.android.networking

import android.content.Context
import io.falu.android.ApiResultCallback
import io.falu.android.models.evaluations.Evaluation
import io.falu.android.models.evaluations.EvaluationRequest
import io.falu.android.models.files.FaluFile
import io.falu.android.models.files.UploadRequest
import io.falu.android.models.payments.Payment
import io.falu.android.models.payments.PaymentRequest
import io.falu.android.utils.getMediaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Makes network requests to the Falu API.
 */
internal class FaluRepository internal constructor(
    private val context: Context,
    publishableKey: String,
    enableLogging: Boolean
) :
    BaseApiRepository(context, publishableKey, enableLogging) {

    /**
     * Create an evaluation asynchronously
     *
     * See [Create an evaluation](https://api.falu.io/v1/evaluations).
     * `POST /v1/evaluations`
     *
     * @param request [The evaluation request object](https://falu.io)
     * @param callbacks [ApiResultCallback] to receive the result or error
     *
     */
    fun createEvaluationAsync(
        request: EvaluationRequest,
        callbacks: ApiResultCallback<Evaluation>
    ) {
        launch(Dispatchers.IO) {
            kotlin.runCatching {
                faluApiClient.createEvaluation(request)
            }.fold(
                onSuccess = {
                    handleFaluResponse(it, callbacks)
                },
                onFailure = {
                    dispatchError(it, callbacks)
                }
            )

        }

    }

    /**
     * Create a payment asynchronously
     *
     * See [Create a payment](https://api.falu.io/v1/payments).
     * `POST /v1/payments`
     *
     * @param request [The payment request object](https://falu.io)
     * @param callbacks [ApiResultCallback] to receive the result or error
     *
     */
    fun createPaymentAsync(request: PaymentRequest, callbacks: ApiResultCallback<Payment>) {
        launch(Dispatchers.IO) {
            runCatching {
                faluApiClient.createPayment(request)
            }.fold(
                onSuccess = {
                    handleFaluResponse(it, callbacks)
                },
                onFailure = {
                    dispatchError(it, callbacks)
                }
            )
        }
    }

    /**
     * Upload a file asynchronously
     *
     * See [Upload a file](https://api.falu.io/v1/file).
     * `POST /v1/files`
     *
     * @param request [The upload request object](https://falu.io)
     * @param callbacks [ApiResultCallback] to receive the result or error
     *
     */
    fun uploadFileAsync(request: UploadRequest, callbacks: ApiResultCallback<FaluFile>) {
        request.mediaType = getMediaType(context, request.file)
        launch(Dispatchers.IO) {
            runCatching {
                faluApiClient.uploadFile(request)
            }.fold(
                onSuccess = {
                    handleFaluResponse(it, callbacks)
                },
                onFailure = {
                    dispatchError(it, callbacks)
                }
            )
        }
    }
}