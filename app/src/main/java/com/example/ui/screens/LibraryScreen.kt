package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Song
import com.example.ui.components.GlassCard
import com.example.ui.theme.AppleMusicRed
import com.example.util.PerformanceMode

data class LibraryMenuItem(
    val title: String,
    val icon: ImageVector,
    val count: Int? = null
)

@Composable
fun LibraryScreen(
    songs: List<Song>,
    downloadedIds: Set<Int>,
    performanceMode: PerformanceMode,
    onSongClick: (Song) -> Unit,
    onDownloadSong: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val downloadedSongs = remember(songs, downloadedIds) {
        songs.filter { downloadedIds.contains(it.id) }
    }

    val displayedSongs = when (selectedCategory) {
        "Downloaded" -> downloadedSongs
        else -> songs
    }

    val menuItems = listOf(
        LibraryMenuItem("Playlists", Icons.AutoMirrored.Filled.QueueMusic, 12),
        LibraryMenuItem("Artists", Icons.Default.Person, 20),
        LibraryMenuItem("Albums", Icons.Default.Album, 18),
        LibraryMenuItem("Songs", Icons.Default.MusicNote, songs.size),
        LibraryMenuItem("Downloaded", Icons.Default.DownloadDone, downloadedSongs.size)
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Library",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                if (selectedCategory != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filtered by $selectedCategory (${displayedSongs.size})",
                            fontSize = 14.sp,
                            color = AppleMusicRed,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Show All",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier
                                .clickable { selectedCategory = null }
                                .padding(4.dp)
                        )
                    }
                }
            }
        }

        // Library Category Links
        item {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(18.dp),
                backgroundColor = Color(0xFF18181C),
                performanceMode = performanceMode
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    menuItems.forEachIndexed { index, item ->
                        val isSelected = selectedCategory == item.title
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedCategory = if (isSelected) null else item.title
                                }
                                .padding(vertical = 13.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = if (isSelected) Color.White else AppleMusicRed,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = item.title,
                                    fontSize = 17.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = Color.White,
                                    modifier = Modifier.padding(start = 14.dp)
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (item.count != null) {
                                    Text(
                                        text = "${item.count}",
                                        fontSize = 14.sp,
                                        color = Color.White.copy(alpha = 0.45f),
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                }
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.3f),
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                        if (index < menuItems.lastIndex) {
                            Divider(color = Color.White.copy(alpha = 0.08f), thickness = 0.5.dp)
                        }
                    }
                }
            }
        }

        // Recently Added Section Title
        item {
            Spacer(modifier = Modifier.height(28.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedCategory == "Downloaded") "Offline Songs" else "Recently Added",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                if (selectedCategory == "Downloaded" && displayedSongs.isEmpty()) {
                    Text(
                        text = "Download songs to listen offline",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Empty state for downloaded
        if (selectedCategory == "Downloaded" && displayedSongs.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No Downloaded Music Yet",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = "Tap the download icon on any song to store it offline",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }

        // Grid of songs
        val chunkedSongs = displayedSongs.chunked(2)
        items(chunkedSongs.size) { chunkIndex ->
            val pair = chunkedSongs[chunkIndex]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                pair.forEach { song ->
                    val isSongDownloaded = downloadedIds.contains(song.id)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSongClick(song) }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            AsyncImage(
                                model = song.artwork,
                                contentDescription = song.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                            )
                            if (isSongDownloaded) {
                                Box(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .align(Alignment.TopEnd)
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(Color.Black.copy(alpha = 0.65f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Downloaded",
                                        tint = Color(0xFF22C55E),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = song.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = song.artist,
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.6f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (pair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}
