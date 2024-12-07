package com.bangkit.dermascan.data.remote.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bangkit.dermascan.R
import com.bangkit.dermascan.ui.main.MainActivity

class MyFCM : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Log data dari pesan yang diterima
        Log.d("FCM", "From: ${remoteMessage.from}")
        remoteMessage.data.isNotEmpty().let {
            Log.d("FCM", "Message data payload: ${remoteMessage.data}")
        }

        // Jika notifikasi juga memiliki payload notifikasi
        remoteMessage.notification?.let {
            Log.d("FCM", "Message Notification Body: ${it.body}")
            showNotification(it.title, it.body)
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
        val intent = Intent(this, MainActivity::class.java) // Ganti dengan activity yang sesuai
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
}
