package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.MusicCatalog
import com.example.player.MediaPlaybackService
import com.example.player.MusicPlayerViewModel
import com.example.util.PerformanceMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Music", appName)
    }

    @Test
    fun `music catalog has songs with video URLs`() {
        assertTrue(MusicCatalog.songs.isNotEmpty())
        assertEquals(20, MusicCatalog.songs.size)
        val first = MusicCatalog.songs.first()
        assertEquals("A Bar Song (Tipsy)", first.title)
        assertEquals("Shaboozey", first.artist)
        assertNotNull(first.mp4Link)
        assertNotNull(first.videoUrl)
    }

    @Test
    fun `view model state transitions and performance modes`() {
        val context = ApplicationProvider.getApplicationContext<android.app.Application>()
        val vm = MusicPlayerViewModel(context)

        val initialState = vm.state.value
        assertNotNull(initialState.currentSong)
        assertEquals(20, initialState.queue.size)
        assertFalse(initialState.isPlaying)

        // Toggle shuffle
        vm.toggleShuffle()
        assertTrue(vm.state.value.isShuffle)

        // Toggle repeat
        vm.toggleRepeat()
        assertTrue(vm.state.value.isRepeat)

        // Toggle favorite
        val songId = initialState.currentSong!!.id
        vm.toggleFavorite(songId)
        assertTrue(vm.state.value.favorites.contains(songId))
        vm.toggleFavorite(songId)
        assertFalse(vm.state.value.favorites.contains(songId))

        // Audio route
        vm.setAudioRoute("AirPods Max")
        assertEquals("AirPods Max", vm.state.value.selectedAudioRoute)

        // Video canvas toggle
        val videoCanvasBefore = vm.state.value.showVideoCanvas
        vm.toggleVideoCanvas()
        assertEquals(!videoCanvasBefore, vm.state.value.showVideoCanvas)

        // Performance mode
        vm.setPerformanceMode(PerformanceMode.BATTERY_SAVER)
        assertEquals(PerformanceMode.BATTERY_SAVER, vm.state.value.performanceMode)
    }

    @Test
    fun `playback state survives app lifecycle and viewmodel recreation`() {
        val context = ApplicationProvider.getApplicationContext<android.app.Application>()
        val vm1 = MusicPlayerViewModel(context)

        val songToPlay = MusicCatalog.songs[2]
        vm1.playSong(songToPlay)
        vm1.toggleFavorite(songToPlay.id)
        vm1.setAudioRoute("Px8 Bowers & Wilkins")

        // Simulate app minimize or activity recreation
        val vm2 = MusicPlayerViewModel(context)
        assertEquals(songToPlay.id, vm2.state.value.currentSong?.id)
        assertTrue(vm2.state.value.favorites.contains(songToPlay.id))
        assertEquals("Px8 Bowers & Wilkins", vm2.state.value.selectedAudioRoute)
    }
}
