package com.ssafy.silencelake.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ssafy.silencelake.activity.admin.AdminActivity
import com.ssafy.silencelake.activity.admin.AdminBroadCastReceiver
import com.ssafy.silencelake.activity.admin.AdminViewModel
import com.ssafy.silencelake.activity.login.LoginActivity
import com.ssafy.silencelake.activity.main.MainActivity
private const val TAG = "MyFirebaseMsgSvc_싸피"

class MyFirebaseMessageService : FirebaseMessagingService() {
    // 새로운 토큰이 생성될 때 마다 해당 콜백이 호출된다.
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")
        // 새로운 토큰 수신 시 서버로 전송
        MainActivity.uploadToken(token)
    }

    // Foreground, Background 모두 처리하기 위해서는 data에 값을 담아서 넘긴다.
    //https://firebase.google.com/docs/cloud-messaging/android/receive
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var messageTitle = ""
        var messageContent = ""

        if(remoteMessage.notification != null){ // notification이 있는 경우 foreground처리
            //foreground
            Log.d(TAG, "onMessageReceived: ")
            messageTitle= remoteMessage.notification!!.title.toString()
            messageContent = remoteMessage.notification!!.body.toString()
            if(messageTitle == "admin"){
                val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent()
                intent.setClass(this, AdminBroadCastReceiver::class.java)
                val pendingIntent =
                    PendingIntent.getBroadcast(this, 111, intent, PendingIntent.FLAG_IMMUTABLE)
                manager[AlarmManager.ELAPSED_REALTIME_WAKEUP, 0] = pendingIntent // 0초 후 발송.
                return
            }
        }else{  // background 에 있을경우 혹은 foreground에 있을경우 두 경우 모두
            var data = remoteMessage.data
            Log.d(TAG, "data.message: ${data}")
            Log.d(TAG, "data.message: ${data.get("title")}")
            Log.d(TAG, "data.message: ${data.get("body")}")

            messageTitle = data.get("title").toString()
            messageContent = data.get("body").toString()
        }

        val mainIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val adminIntent = Intent(this, AdminActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val mainPendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE)
        val adminPendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, adminIntent, PendingIntent.FLAG_IMMUTABLE)
        val builder1 = NotificationCompat.Builder(this, LoginActivity.channel_id)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(messageTitle)
            .setContentText(messageContent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
        if(messageTitle == "admin"){
            builder1.setContentIntent(adminPendingIntent)
        }else{
            builder1.setContentIntent(mainPendingIntent)
        }
        NotificationManagerCompat.from(this).apply {
            notify(101, builder1.build())
        }
    }

}