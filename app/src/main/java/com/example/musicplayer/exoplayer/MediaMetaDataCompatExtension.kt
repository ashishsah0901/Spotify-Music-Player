package com.example.musicplayer.exoplayer

import android.support.v4.media.MediaMetadataCompat
import com.example.musicplayer.data.Song
//This is an extension function which which is used to convert the mediaMetaData to custom song class
fun MediaMetadataCompat.toSong(): Song? {
    return description?.let {
        Song(
            it.mediaId ?: "",
            it.title.toString(),
            it.subtitle.toString(),
            it.mediaUri.toString(),
            it.iconUri.toString()
        )
    }
}