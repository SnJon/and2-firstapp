package ru.netology.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.R
import kotlin.random.Random

private const val KEY_ACTION = "action"
private const val KEY_CONTENT = "content"
private const val CHANNEL_ID = "remote"

class FCMService : FirebaseMessagingService() {

    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {

        message.data[KEY_ACTION]?.let {
            when (Action.getOrNull(it)) {
                Action.LIKE -> handleLike(
                    gson.fromJson(
                        message.data[KEY_CONTENT],
                        Like::class.java
                    )
                )
                Action.NEW_POST -> handleNewPost(
                    gson.fromJson(
                        message.data[KEY_CONTENT],
                        NewPost::class.java
                    )
                )
                else -> Unit
            }
        }
    }

    override fun onNewToken(token: String) {
        println(token)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleLike(content: Like) {

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_liked,
                    content.userName,
                    content.postAuthor,
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionRequest()
        }

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleNewPost(content: NewPost) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_new_post,
                    content.userName,
                )
            )
            .setStyle(NotificationCompat.BigTextStyle().bigText(content.postText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionRequest()
        }

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun notificationPermissionRequest() {
        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            putExtra(Settings.EXTRA_CHANNEL_ID, CHANNEL_ID)
        }
        startActivity(intent)
    }
}

enum class Action(val title: String) {
    LIKE("Like"),
    NEW_POST("New Post");

    companion object {
        fun getOrNull(title: String): Action? {
            return Action.values()
                .firstOrNull { it.title.equals(title, ignoreCase = true) }
        }
    }
}

data class Like(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String,
)

data class NewPost(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postText: String,
)