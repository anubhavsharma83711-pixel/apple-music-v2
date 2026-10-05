package com.example.player

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Song
import com.example.util.PerformanceMode
import kotlinx.coroutines.flow.StateFlow

class MusicPlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val engine = MediaPlaybackService.engine

    val state: StateFlow<MusicPlayerState> = engine.state
    val positionMs: StateFlow<Long> = engine.positionMs

    init {
        // Start foreground playback service
        MediaPlaybackService.startService(application)
        engine.checkDownloadedSongs(application)
    }

    fun playSong(song: Song) {
        MediaPlaybackService.startService(getApplication())
        engine.playSong(song)
    }

    fun togglePlayPause() {
        MediaPlaybackService.startService(getApplication())
        engine.togglePlayPause()
    }

    fun playNext() {
        engine.playNext()
    }

    fun playPrevious() {
        engine.playPrevious()
    }

    fun seekTo(positionMs: Long) {
        engine.seekTo(positionMs)
    }

    fun setVolume(volume: Float) {
        engine.setVolume(volume)
    }

    fun toggleShuffle() {
        engine.toggleShuffle()
    }

    fun toggleRepeat() {
        engine.toggleRepeat()
    }

    fun toggleFavorite(songId: Int) {
        engine.toggleFavorite(songId)
    }

    fun toggleLyrics() {
        engine.toggleLyrics()
    }

    fun toggleQueue() {
        engine.toggleQueue()
    }

    fun toggleVideoCanvas() {
        engine.toggleVideoCanvas()
    }

    fun setPerformanceMode(mode: PerformanceMode) {
        engine.setPerformanceMode(mode)
    }

    fun setShowAudioRoute(show: Boolean) {
        engine.setShowAudioRoute(show)
    }

    fun setAudioRoute(route: String) {
        engine.setAudioRoute(route)
    }

    fun shuffleAll() {
        MediaPlaybackService.startService(getApplication())
        engine.shuffleAll()
    }

    fun downloadSong(song: Song) {
        engine.downloadSong(getApplication(), song, viewModelScope)
    }

    fun deleteDownload(songId: Int) {
        engine.deleteDownload(getApplication(), songId, viewModelScope)
    }
}
