package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.MusicCatalog
import com.example.player.MusicPlayerViewModel
import com.example.ui.components.AppleMusicBottomBar
import com.example.ui.components.NavigationTab
import com.example.ui.player.AppleMusicSheetLayout
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.NewScreen
import com.example.ui.screens.RadioScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = true) {
                AppleMusicApp()
            }
        }
    }
}

@Composable
fun AppleMusicApp(
    viewModel: MusicPlayerViewModel = viewModel()
) {
    val playerState by viewModel.state.collectAsStateWithLifecycle()
    var currentTab by remember { mutableStateOf(NavigationTab.HOME) }
    val allSongs = MusicCatalog.songs

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AppleMusicSheetLayout(
            playerState = playerState,
            viewModel = viewModel,
            bottomBar = {
                AppleMusicBottomBar(
                    currentTab = currentTab,
                    performanceMode = playerState.performanceMode,
                    onTabSelected = { currentTab = it }
                )
            },
            backgroundContent = {
                Crossfade(targetState = currentTab, label = "tabCrossfade") { tab ->
                    when (tab) {
                        NavigationTab.HOME -> {
                            HomeScreen(
                                songs = allSongs,
                                currentSong = playerState.currentSong,
                                isPlaying = playerState.isPlaying,
                                downloadedIds = playerState.downloadedSongIds,
                                performanceMode = playerState.performanceMode,
                                onSongClick = { viewModel.playSong(it) },
                                onPlayFirst = {
                                    allSongs.firstOrNull()?.let { viewModel.playSong(it) }
                                },
                                onShuffleAll = { viewModel.shuffleAll() },
                                onSetPerformanceMode = { viewModel.setPerformanceMode(it) }
                            )
                        }
                        NavigationTab.NEW -> {
                            NewScreen(
                                songs = allSongs,
                                onSongClick = { viewModel.playSong(it) }
                            )
                        }
                        NavigationTab.RADIO -> {
                            RadioScreen(
                                songs = allSongs,
                                onTuneIn = { viewModel.playSong(it) }
                            )
                        }
                        NavigationTab.LIBRARY -> {
                            LibraryScreen(
                                songs = allSongs,
                                downloadedIds = playerState.downloadedSongIds,
                                performanceMode = playerState.performanceMode,
                                onSongClick = { viewModel.playSong(it) },
                                onDownloadSong = { viewModel.downloadSong(it) }
                            )
                        }
                        NavigationTab.SEARCH -> {
                            SearchScreen(
                                songs = allSongs,
                                onSongClick = { viewModel.playSong(it) }
                            )
                        }
                    }
                }
            }
        )
    }
}
