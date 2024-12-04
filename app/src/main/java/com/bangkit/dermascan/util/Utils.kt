package com.bangkit.dermascan.util

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import com.bangkit.dermascan.data.model.response.message.ErrorMessage
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

private const val MAXIMAL_SIZE = 1000000
private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

fun parseErrorMessage(errorBody: String?): String {
    return try {
        val gson = Gson()  // Pastikan sudah menambahkan Gson di dependencies
        val error = gson.fromJson(errorBody, ErrorMessage::class.java)
        when (error.error) {
            is String -> error.error  // Jika error adalah String tunggal
            is List<*> -> (error.error as List<*>).joinToString(", ")  // Jika error adalah List
            else -> error.message  // Default pesan error
        }
    } catch (e: Exception) {
        "Error parsing response."
    }
}

fun getBitmapFromUri(context: Context, imageUri: Uri): Bitmap? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val rotatedBitmap = rotateBitmapIfNeeded(context, imageUri, bitmap)
        rotatedBitmap
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun compressImageFromUri(context: Context, imageUri: Uri, maxSizeMB: Int): ByteArray? {
    val bitmap = getBitmapFromUri(context, imageUri)
    return if (bitmap != null) {
        compressToMaxSize(bitmap, maxSizeMB)
    } else {
        null
    }
}

fun rotateBitmapIfNeeded(context: Context, imageUri: Uri, bitmap: Bitmap): Bitmap {
    val exifInterface = ExifInterface(context.contentResolver.openInputStream(imageUri)!!)
    val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
        else -> bitmap // Jika tidak ada rotasi, kembalikan bitmap tanpa perubahan
    }
}

fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

fun compressToMaxSize(bitmap: Bitmap, maxSizeMB: Int): ByteArray {
    val maxSizeBytes = maxSizeMB * 1024 *1024// Konversi MB ke Bytes
    Log.d("Original Max Bytes", "Size: $maxSizeBytes")
    Log.d("Original Image Size", "Size: ${bitmap.byteCount}")
    var quality = 100  // Kualitas awal
    var compressedImage: ByteArray
    val outputStream = ByteArrayOutputStream()

    do {
        outputStream.reset()  // Reset stream untuk iterasi ulang
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        compressedImage = outputStream.toByteArray()
        quality -= 5  // Kurangi kualitas bertahap
    } while (compressedImage.size > maxSizeBytes && quality > 0)
    Log.d("Compressed Image Size", "Size: ${compressedImage.size}")
    return compressedImage
}

fun prepareFilePart(file: File): MultipartBody.Part {
    val file = File(file.path) // Pastikan untuk mendapatkan path file yang benar
    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull()) // Tipe MIME gambar
    return MultipartBody.Part.createFormData("image", file.name, requestFile)
}

fun uriToFile(imageUri: Uri, context: Context): File {
    val myFile = createCustomTempFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val outputStream = FileOutputStream(myFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
    outputStream.close()
    inputStream.close()
    return myFile
}

fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}

fun File.reduceFileImage(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun Bitmap.getRotatedBitmap(file: File): Bitmap {
    val orientation = ExifInterface(file).getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90F)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180F)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270F)
        ExifInterface.ORIENTATION_NORMAL -> this
        else -> this
    }
}

fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height, matrix, true
    )
}

fun byteArrayToUri(context: Context, byteArray: ByteArray, fileName: String): Uri? {
    // Buat file sementara di cache directory
    val file = File(context.cacheDir, fileName)

    try {
        // Menulis byteArray ke file
        val fos = FileOutputStream(file)
        fos.write(byteArray)
        fos.close()

        // Mengembalikan Uri menggunakan FileProvider
        return FileProvider.getUriForFile(context, "com.bangkit.dermascan.provider", file)
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
}

fun formatTimestamp(input: String): String {
    // Parsing input string ke ZonedDateTime
    val zonedDateTime = ZonedDateTime.parse(input)

    // Mengubah format sesuai kebutuhan
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH.mm")

    // Mengonversi dan mengembalikan hasil
    return zonedDateTime.format(dateFormatter)
}

/*using prepareFilePart():
val imagePart = prepareFilePart(imageUri) // Mendapatkan file part dari URI gambar
val response = apiService.uploadSkinImage(imagePart)*/