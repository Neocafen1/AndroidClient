

package com.example.neocafeteae1prototype

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.neocafeteae1prototype.view.tools.firebaseLogging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class   MyFireBaseService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        p0.firebaseLogging()
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        if (p0.notification != null){
            sendNotification(p0.notification!!.title!!, p0.notification!!.body!!)
        }

    }

    private fun sendNotification(title:String, body:String) {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.drawable.ic_call)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentText(body)
            .setSound(sound)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "1",
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, builder.build())

    }
}