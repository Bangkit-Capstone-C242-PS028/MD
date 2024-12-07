package com.bangkit.dermascan.data.remote.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bangkit.dermascan.R
import com.bangkit.dermascan.ui.article.ArticleActivity
import com.bangkit.dermascan.ui.main.MainActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class MyFCM : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Log data dari pesan yang diterima
        Log.d("FCM", "From: ${remoteMessage.from}")
        remoteMessage.data.isNotEmpty().let {
            Log.d("FCM", "Message data payload: ${remoteMessage.data}")
        }

        // Jika notifikasi juga memiliki payload notifikasi
        Log.d("FCM", "From: ${remoteMessage.from}")
        remoteMessage.notification?.let { notification ->
            val title = notification.title
            val body = notification.body
            val imageUrl = notification.imageUrl
//            notification.bodyLocalizationKey

            Log.d("FCM", "Notification image URL: $imageUrl")
            if (imageUrl != null) {
                // Tampilkan notifikasi dengan gambar
                showNotificationWithImage(title, body, imageUrl.toString())
            } else {
                // Tampilkan notifikasi biasa
                showNotification(title, body)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Refreshed token: $token")
        // Kirim token ke server backend Anda jika diperlukan
    }

    private fun showNotification(title: String?, body: String?) {
        // ID unik untuk setiap notifikasi
        val notificationId = System.currentTimeMillis().toInt()

        // Intent untuk membuka aplikasi saat notifikasi di-tap
        val intent = Intent(this, ArticleActivity::class.java) // Ganti dengan activity yang sesuai
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // Channel ID untuk Android Oreo ke atas
        val channelId = "article_notifications"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Buat Notification Channel jika di Android Oreo ke atas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Article Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Bangun notifikasi
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notif) // Ganti dengan ikon Anda
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        // Tampilkan notifikasi
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun showNotificationWithImage(title: String?, body: String?, imageUrl: String) {
        val notificationId = System.currentTimeMillis().toInt()

        // Intent untuk membuka aplikasi saat notifikasi di-tap
        val intent = Intent(this, ArticleActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "article_notifications"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Article Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Muat gambar menggunakan Glide
        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val notificationBuilder = NotificationCompat.Builder(this@MyFCM, channelId)
                        .setSmallIcon(R.drawable.ic_notif) // Ganti dengan ikon Anda
                        .setContentTitle(title)
                        .setContentText(body)
                        .setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)

                    notificationManager.notify(notificationId, notificationBuilder.build())
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Jika gambar tidak bisa dimuat
                }
            })
    }

}
