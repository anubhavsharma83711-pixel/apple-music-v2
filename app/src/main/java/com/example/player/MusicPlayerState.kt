package com.example.player

import com.example.model.Song
import com.example.util.PerformanceMode

data class MusicPlayerState(
    val currentSong: Song? = null,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val durationMs: Long = 30000L,
    val isShuffle: Boolean = false,
    val isRepeat: Boolean = false,
    val volume: Float = 0.8f,
    val favorites: Set<Int> = emptySet(),
    val queue: List<Song> = emptyList(),
    val showLyrics: Boolean = false,
    val showQueue: Boolean = false,
    val showAudioRoute: Boolean = false,
    val selectedAudioRoute: String = "iPhone Speaker",
    val showVideoCanvas: Boolean = true,
    val downloadedSongIds: Set<Int> = emptySet(),
    val downloadProgress: Map<Int, Float> = emptyMap(),
    val performanceMode: PerformanceMode = PerformanceMode.HIGH
)
