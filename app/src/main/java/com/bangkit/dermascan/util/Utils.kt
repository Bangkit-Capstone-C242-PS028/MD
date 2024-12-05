package com.bangkit.dermascan.util

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ContentValues
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
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.bangkit.dermascan.BuildConfig
import com.bangkit.dermascan.R
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
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



fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year)
            onDateSelected(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    Log.d("DatePicker", "Dialog dipanggil")
    datePickerDialog.show()
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

fun getImageUri(context: Context): Uri {
    var uri: Uri? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera")
        }
        uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }
    return uri ?: getImageUriForPreQ(context)
}

private fun getImageUriForPreQ(context: Context): Uri {
    val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File(filesDir, "/MyCamera/$timeStamp.jpg")
    if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdirs()

    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.fileprovider",
        imageFile
    )
}

@SuppressLint("StringFormatMatches")
fun formatTime(context: Context, createdAt: String?): String {
    if (createdAt.isNullOrEmpty()) return context.getString(R.string.time_is_not_available)


    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val instant = Instant.from(formatter.parse(createdAt))


    val now = Instant.now()
    val duration = java.time.Duration.between(instant, now)


    return when {
        duration.toDays() > 0 -> context.getString(R.string.days_a_go, duration.toDays())
        duration.toHours() > 0 -> context.getString(R.string.hours_a_go, duration.toHours())
        duration.toMinutes() > 0 -> context.getString(R.string.minute_a_go, duration.toMinutes())
        else -> context.getString(R.string.now)
    }
}

/*using prepareFilePart():
val imagePart = prepareFilePart(imageUri) // Mendapatkan file part dari URI gambar
val response = apiService.uploadSkinImage(imagePart)*/