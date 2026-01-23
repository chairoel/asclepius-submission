package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import org.tensorflow.lite.task.vision.classifier.ImageClassifier.ImageClassifierOptions
import java.util.Locale

class ImageClassifierHelper(
    private val context: Context,
    private val modelName: String = "cancer_classification.tflite",
    private val threshold: Float = 0.1f,
    private val maxResults: Int = 3
) {

    private var imageClassifier: ImageClassifier? = null
    var predictionResult: String? = null

    private fun setupImageClassifier() {
        // TODO: Menyiapkan Image Classifier untuk memproses gambar.
        val optionsBuilder = ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)

        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)

        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: Exception) {
            Log.e(TAG, "Setup gagal: ${e.message}")
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        // TODO: mengklasifikasikan imageUri dari gambar statis.
        if (imageClassifier == null) {
            setupImageClassifier()
        }

        val bitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                android.graphics.BitmapFactory.decodeStream(inputStream)
            }
        } ?: return

        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .add(CastOp(DataType.UINT8))
            .build()

        val tensorImage = imageProcessor.process(
            TensorImage.fromBitmap(mutableBitmap)
        )

        val results = imageClassifier?.classify(tensorImage)

        predictionResult = results?.firstOrNull()?.categories
            ?.maxByOrNull { it.score }
            ?.let { category ->
                "${category.label} (${
                    String.format(
                        Locale.getDefault(), "%.1f%%", category.score * 100
                    )
                })"
            }

        Log.d(TAG, "Prediksi: $predictionResult")
    }


    companion object {
        private var TAG = ImageClassifierHelper::class.java.simpleName
    }
}
