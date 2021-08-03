package io.falu.android


import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.falu.android.model.PaymentInitiationMpesa
import io.falu.android.model.PaymentRequest
import io.falu.android.model.evaluations.EvaluationRequest
import io.falu.android.model.evaluations.EvaluationScope
import io.falu.android.networking.FaluApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(manifest = "AndroidManifest.xml")
@ExperimentalCoroutinesApi
class FaluEndToEndTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val apiClient = FaluApiClient(context, FakeKeys.TEST_PUBLISHABLE_KEY, true)

    private val testDispatcher = TestCoroutineDispatcher()

    @AfterTest
    fun cleanup() {
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testCreateEvaluationThrowsException() {
        val file = File(context.cacheDir, "falu.pdf")
        val fileStream = context.resources.openRawResource(R.raw.falu)
        copyStreamToFile(fileStream, file)

        val request = EvaluationRequest(
            scope = EvaluationScope.PERSONAL,
            name = "JOHN DOE",
            phone = "+2547123456789",
            password = "12345678",
            file = file,
        )

        runBlocking(Dispatchers.IO) {
            val response = apiClient.createEvaluation(request)
            assertEquals(true, !response.successful())
        }
    }

    @Test
    fun testMpesaPaymentInitRequest() {
        val mpesa = PaymentInitiationMpesa()
        mpesa.phone = "+254712345678"
        mpesa.reference = "254712345678"
        mpesa.paybill = true

        val request = PaymentRequest(
            amount = 100,
            currency = "kes",
            mpesa = mpesa
        )

        runBlocking(Dispatchers.IO) {
            val response = apiClient.createPayment(request)
            assertEquals(true, response.successful())
        }
    }

    private fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }
    }
}