package com.example.model

data class Song(
    val id: Int,
    val title: String,
    val artist: String,
    val album: String = "",
    val artwork: String,
    val mp4Link: String,
    val videoUrl: String? = null,
    val artworkBgColor: String = "#333333",
    val durationMs: Long = 30000L,
    val lyrics: List<String> = emptyList(),
    val isDownloaded: Boolean = false,
    val localFilePath: String? = null
)
