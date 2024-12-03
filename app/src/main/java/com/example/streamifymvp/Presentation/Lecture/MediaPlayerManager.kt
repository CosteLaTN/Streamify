package com.example.streamifymvp.Presentation.Lecture

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.streamifymvp.Domaine.entitees.Chanson
import java.io.IOException

object MediaPlayerManager {
    private var mediaPlayer: MediaPlayer? = null
    private var currentChanson: Chanson? = null
    private val _isPlaying = MutableLiveData(false)
    private val _currentPosition = MutableLiveData(0)
    private var isRepeatMode = false

    val isPlaying: LiveData<Boolean> get() = _isPlaying
    val currentPosition: LiveData<Int> get() = _currentPosition

    fun playChanson(chanson: Chanson, startPosition: Int = 0) {
        if (chanson.fichierAudio.isNullOrBlank()) {
            Log.e("MediaPlayerManager", "Fichier audio introuvable pour la chanson : ${chanson.nom}")
            _isPlaying.value = false
            return
        }

        if (currentChanson != chanson) {
            releaseMediaPlayer()
            try {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(chanson.fichierAudio)
                    prepare()
                    seekTo(startPosition)
                    start()
                    setOnCompletionListener {
                        if (isRepeatMode) {
                            seekTo(0)
                            start()
                        } else {
                            _isPlaying.value = false
                        }
                    }
                }
                currentChanson = chanson
            } catch (e: IOException) {
                Log.e("MediaPlayerManager", "Erreur lors de la préparation du MediaPlayer : ${e.message}")
                _isPlaying.value = false
            } catch (e: IllegalStateException) {
                Log.e("MediaPlayerManager", "État illégal pour MediaPlayer : ${e.message}")
                _isPlaying.value = false
            }
        } else {
            resume()
        }
        _isPlaying.value = true
        startProgressUpdate()
    }



    fun pause() {
        mediaPlayer?.pause()
        _isPlaying.value = false
    }

    fun resume() {
        mediaPlayer?.start()
        _isPlaying.value = true
        startProgressUpdate()
    }

    fun stop() {
        mediaPlayer?.stop()
        _isPlaying.value = false
    }

    fun setRepeatMode(enabled: Boolean) {
        isRepeatMode = enabled
    }

    fun getMediaPlayer(): MediaPlayer? {
        return mediaPlayer
    }

    fun getCurrentPosition(): Int {
        return if (isMediaPlayerValid()) {
            try {
                mediaPlayer?.currentPosition ?: 0
            } catch (e: IllegalStateException) {
                Log.e("MediaPlayerManager", "Erreur lors de la récupération de la position actuelle : ${e.message}")
                0
            }
        } else {
            0
        }
    }



    private fun startProgressUpdate() {
        Thread {
            while (isMediaPlayerValid()) {
                try {
                    _currentPosition.postValue(getCurrentPosition())
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    Log.e("MediaPlayerManager", "Thread interrompu : ${e.message}")
                    Thread.currentThread().interrupt()
                }
            }
            Log.d("MediaPlayerManager", "Arrêt du suivi de progression car le MediaPlayer est invalide.")
        }.start()
    }


    fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        _isPlaying.value = false
    }
    fun isMediaPlayerValid(): Boolean {
        return mediaPlayer != null && _isPlaying.value == true
    }
    fun MediaPlayer.isPlayingCurrentChanson(chanson: Chanson): Boolean {
        return currentChanson?.id == chanson.id
    }



}


