package com.example.musicplayer.exoplayer.callbacks

import android.widget.Toast
import com.example.musicplayer.exoplayer.MusicService
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
//This is called when the playback state of the player is changed, it is used to stop the service is the state is ready
//and and we don't have to play the music
class MusicPlayerEventListener(
    private val musicService: MusicService
) : Player.Listener {

    private var playWhen: Boolean = false

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        if(playbackState == Player.STATE_READY && !playWhen) {
            musicService.stopForeground(false)
        }
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)
        playWhen = playWhenReady
    }

    override fun onPlayerErrorChanged(error: PlaybackException?) {
        super.onPlayerErrorChanged(error)
        Toast.makeText(musicService, error?.message.toString(), Toast.LENGTH_LONG).show()
    }

}