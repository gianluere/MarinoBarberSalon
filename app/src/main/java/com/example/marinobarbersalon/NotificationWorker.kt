package com.example.marinobarbersalon

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val titolo = inputData.getString("title") ?: "Promemoria Appuntamento"
        val messaggio = inputData.getString("message") ?: "Non dimenticarti del tuo appuntamento oggi!"

        val channelId = "appointment_reminder_channel"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crea il canale di notifica (necessario per Android 8+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Promemoria Appuntamenti",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Intent per aprire l'app quando si clicca sulla notifica
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Configura la notifica
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(titolo)
            .setContentText(messaggio)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Mostra la notifica
        notificationManager.notify(1, notification)

        return Result.success()
    }

}
