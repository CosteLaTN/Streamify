package com.example.streamifymvp.Presentation.EcranChansonsLDL

import android.util.Log
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import com.example.streamifymvp.Presentation.Modele
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EcranChansonsLDLPresentateurTests {

    private lateinit var modele: Modele
    private lateinit var presentateur: EcranChansonsLDLPresentateur

    @Before
    fun setup() {

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        modele = mockk()
        presentateur = EcranChansonsLDLPresentateur(modele)
    }

    @Test
    fun `etant donne un id de playlist lorsqu'on appelle obtenirChansonsDeLaListeDeLecture alors retourner la liste des chansons`() {
        // Arrange
        val playlistId = 1
        val chansons = listOf(Chanson(1, "Song 1", "2022-01-01", "Pop", "3:45", "3:45", 0, 0, 1, 1))
        val playlist = ListeDeLecture(playlistId, "Playlist 1", chansons.toMutableList())
        every { modele.obtenirListeDeLectureParId(playlistId) } returns playlist

        // Act
        val result = presentateur.obtenirChansonsDeLaListeDeLecture(playlistId)

        // Assert
        assertEquals(chansons, result)
    }
}
