package com.gmail.devpelegrino.pomodorotimer.util

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes

class MediaPlayerUtils(private val context: Context) {
    private val mediaPlayer = MediaPlayer().apply {
        setOnPreparedListener { start() }
        setOnCompletionListener { reset() }
    }

    fun playSound(@RawRes rawResId: Int) {
        val assetFileDescriptor = context.resources.openRawResourceFd(rawResId) ?: return
        mediaPlayer.run {
            reset()
            setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.declaredLength)
            prepareAsync()
        }
    }

    /*
    * chamar o método release do próprio mediaPlayer e receber null para desalocar os recursos
    * */
}