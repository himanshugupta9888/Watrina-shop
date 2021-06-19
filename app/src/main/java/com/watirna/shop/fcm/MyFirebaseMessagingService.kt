package com.watirna.shop.fcm

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import com.watirna.shop.views.dashboard.DashboardActivity
import javax.inject.Inject

class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var sessionManager: SessionManager
    init {
        AppController.appComponent.inject(this)
    }
    var notificationId = 0
    override fun onNewToken(s: String) {
        @SuppressLint("HardwareIds") val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        Log.d("DEVICE_ID: ", deviceId)
        Log.d("FCM_TOKEN", s)
       // sessionManager = AppController.run { getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE) }
        sessionManager.put(PreferenceKey.DEVICE_TOKEN, s)
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
// [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val intent = Intent(INTENT_FILTER)
        sendBroadcast(intent)
        // [START_EXCLUDE]
// There are two types of messages data messages and notification messages. Data messages are handled
// here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
// traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
// is in the foreground. When the app is in the background an automatically generated notification is displayed.
// When the user taps on the notification they are returned to the app. Messages containing both notification
// and data payloads are treated as notification messages. The Firebase console always sends notification
// messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
// [END_EXCLUDE]
// TODO(developer): Handle FCM messages here.
// Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.from)
        Log.d(TAG, "Message data payload: " + remoteMessage.data)
        if (remoteMessage.data.size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            sendNotification(remoteMessage.data["message"])
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
// message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]
    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private fun scheduleJob() { // [START dispatch_job]
        val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(this))
        val myJob = dispatcher.newJobBuilder()
                .setService(MyJobService::class.java)
                .setTag("my-job-tag")
                .build()
        dispatcher.schedule(myJob)
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageBody: String?) {
        if (!isBackground(applicationContext)) { // app is in foreground, broadcast the push message

        } else {
            // app is in background, show the notification in notification tray
            if (messageBody.equals("New Incoming Ride", ignoreCase = true)
                    || messageBody.equals("New Incoming Order Request", ignoreCase = true)
                    || messageBody.equals("New Incoming Service Request", ignoreCase = true)
                    || messageBody.equals("RRRR")
                    || messageBody.equals("TRANSPORT",true)
                    || messageBody.equals("ORDER" ,true)
                    || messageBody.equals("SERVICE" ,true)) {
                val mainIntent = Intent(this, DashboardActivity::class.java)
                mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(mainIntent)
            }
        }
        val intent = Intent(applicationContext, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val mBuilder = NotificationCompat.Builder(this, "PUSH")
        val inboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.addLine(messageBody)
        val `when` = System.currentTimeMillis() // notification time
        // Sets an ID for the notification, so it can be updated.
        val notifyID = 1
        val CHANNEL_ID = "my_channel_01" // The id of the channel.
        val name: CharSequence = "Channel human readable title" // The user-visible name of the channel.
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notification: Notification
        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(getString(R.string.app_name)).setWhen(`when`)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentIntent(pendingIntent)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
                .setWhen(`when`)
                .setSmallIcon(getNotificationIcon(mBuilder))
                .setContentText(messageBody)
                .setChannelId(CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_VIBRATE
                        or Notification.DEFAULT_LIGHTS)
                .build()
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            // Create a notification and set the notification channel.
            notificationManager.createNotificationChannel(mChannel)
        }
        notificationManager.notify(0, notification)
    }

    private fun getNotificationIcon(notificationBuilder: NotificationCompat.Builder): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.color = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
            R.mipmap.ic_launcher
        } else {
            R.mipmap.ic_launcher
        }
    }

    private fun isBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses = am.runningAppProcesses
        for (processInfo in runningProcesses)
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                for (activeProcess in processInfo.pkgList)
                    if (activeProcess == context.packageName) isInBackground = false
        return isInBackground
    }

    private fun isCallActive(context: Context): Boolean {
        val manager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return manager.mode == AudioManager.MODE_IN_CALL
    }

    private fun isLocked(context: Context): Boolean {
        val myKM = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return myKM.isKeyguardLocked
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        const val INTENT_FILTER = "INTENT_FILTER"
    }
}