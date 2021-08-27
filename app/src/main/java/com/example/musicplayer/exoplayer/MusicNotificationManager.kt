package com.example.musicplayer.exoplayer

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.musicplayer.R
import com.example.musicplayer.util.Constants.NOTIFICATION_CHANNEL_ID
import com.example.musicplayer.util.Constants.NOTIFICATION_ID
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
//This takes care of all the stuffs related to the player notification. To make the notification exoplayer has it's
// notification manager which helps in creating and interacting easier. To update the notification we need a
//description adapter which helps in updating the notification with the the current playing music. Here we have a
// notification listener which listens to the interaction with the notification and acts accordingly. For the notification
//we need a session token which helps us bind the notification to that media session
class MusicNotificationManager(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener,
    private val newSongCallback: () -> Unit
) {

    private val notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)
        notificationManager = PlayerNotificationManager.Builder(
                context,
                NOTIFICATION_ID,
                NOTIFICATION_CHANNEL_ID
        )
                .setNotificationListener(notificationListener)
                .setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
                .setChannelDescriptionResourceId(R.string.notification_channel_description)
                .setChannelNameResourceId(R.string.notification_channel_name)
                .build()
                .apply {
                    setMediaSessionToken(sessionToken)
                    setSmallIcon(R.drawable.ic_music)
                }

    }

    fun showNotification(player: Player) {
        notificationManager.setPlayer(player)
    }

    private inner class DescriptionAdapter(
        private val mediaController: MediaControllerCompat
    ) : PlayerNotificationManager.MediaDescriptionAdapter {

        override fun getCurrentContentTitle(player: Player): CharSequence {
            newSongCallback()
            return mediaController.metadata.description.title.toString()
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return mediaController.sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence {
            return mediaController.metadata.description.subtitle.toString()
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            Glide.with(context).asBitmap()
                .load(mediaController.metadata.description.iconUri)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        callback.onBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) = Unit
                })
            return null
        }
    }
}
