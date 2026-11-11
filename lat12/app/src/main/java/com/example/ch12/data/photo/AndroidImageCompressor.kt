package com.example.ch12.data.photo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import com.example.ch12.util.PROFILE_PHOTO_QUALITY
import com.example.ch12.util.PROFILE_PHOTO_SIZE
import com.example.ch12.util.calculateInSampleSize
import com.example.ch12.util.centerSquare
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

// Foto 4000x3000 -> JPEG persegi 400x400, sekitar 30-60 KB
class AndroidImageCompressor @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageCompressor {

    // Decode dan kompres JPEG adalah kerja CPU + I/O: jangan di main thread
    override suspend fun compress(uri: String): CompressedImage = withContext(Dispatchers.Default) {
        val source = Uri.parse(uri)

        // Dekode dua tahap:
        // 1. Baca ukuran bounds gambar tanpa memuat seluruh piksel ke memori
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        open(source).use { BitmapFactory.decodeStream(it, null, bounds) }
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) {
            throw ImageDecodeException("Berkas yang dipilih bukan gambar.")
        }

        // 2. Dekode ulang dengan downsampling (inSampleSize)
        val sampleOptions = BitmapFactory.Options().apply {
            inSampleSize = calculateInSampleSize(bounds.outWidth, bounds.outHeight, PROFILE_PHOTO_SIZE)
        }
        val sampled = open(source).use { BitmapFactory.decodeStream(it, null, sampleOptions) }
            ?: throw ImageDecodeException("Gagal mendekode gambar.")

        // 3. Potong persegi di tengah (centerSquare) + skala ke ukuran profil + rotasi EXIF
        val crop = centerSquare(sampled.width, sampled.height)
        val scale = minOf(1f, PROFILE_PHOTO_SIZE.toFloat() / crop.size)
        val matrix = Matrix().apply {
            postScale(scale, scale)
            val rotation = readRotation(source).toFloat()
            if (rotation != 0f) {
                postRotate(rotation)
            }
        }

        val resultBitmap = Bitmap.createBitmap(
            sampled,
            crop.left,
            crop.top,
            crop.size,
            crop.size,
            matrix,
            true
        )

        if (resultBitmap !== sampled) {
            sampled.recycle()
        }

        // 4. Kompres hasil akhir ke format JPEG
        val out = ByteArrayOutputStream()
        resultBitmap.compress(Bitmap.CompressFormat.JPEG, PROFILE_PHOTO_QUALITY, out)
        val bytes = out.toByteArray()
        val finalWidth = resultBitmap.width
        val finalHeight = resultBitmap.height
        resultBitmap.recycle()

        // 5. Kembalikan data CompressedImage
        CompressedImage(
            bytes = bytes,
            originalWidth = bounds.outWidth,
            originalHeight = bounds.outHeight,
            width = finalWidth,
            height = finalHeight
        )
    }

    // content:// (Photo Picker) maupun file:// (CameraX) dibuka lewat ContentResolver
    private fun open(uri: Uri): InputStream =
        context.contentResolver.openInputStream(uri)
            ?: throw ImageDecodeException("Gambar tidak dapat dibuka.")

    // Kamera menyimpan piksel apa adanya + tag orientasi. BitmapFactory
    // mengabaikan tag itu, sehingga tanpa ini foto potret bisa tampil miring.
    private fun readRotation(uri: Uri): Int = runCatching {
        open(uri).use { ExifInterface(it).rotationDegrees }
    }.getOrDefault(0)
}
