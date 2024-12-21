package com.example.streamifymvp.Presentation.EcranChansonsLDL

import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import com.example.streamifymvp.Presentation.Modele
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EcranChansonsLDLPresentateurTests {

    private lateinit var mockModèle: Modele
    private lateinit var présentateur: EcranChansonsLDLPresentateur

    @Before
    fun initMocks() {
        mockModèle = Mockito.mock(Modele::class.java)
        présentateur = EcranChansonsLDLPresentateur(mockModèle)
    }

    @Test
    fun `étant donné un id de playlist valide lorsqu'on obtient les chansons de la playlist on obtient une liste non vide`() = runTest {
        val playlistId = 1
        val chansons = listOf(
            Chanson(
                id = 101,
                nom = "Chanson 1",
                datePublication = "2023-01-01",
                genre = "Pop",
                dureeAlbum = "3:45",
                dureeMusique = "3:45",
                imageChanson = "image_url",
                fichierAudio = "audio_url",
                albumId = 1,
                artiste = null
            )
        )
        val playlist = ListeDeLecture(
            id = playlistId,
            nom = "Ma Playlist",
            chansons = chansons.toMutableList()
        )

        Mockito.`when`(mockModèle.obtenirListeDeLectureParId(playlistId)).thenReturn(playlist)

        val résultat = présentateur.obtenirChansonsDeLaListeDeLecture(playlistId)

        assertNotNull(résultat)
        assertTrue(résultat.isNotEmpty())
        assertEquals("Chanson 1", résultat.first().nom)
    }

    @Test
    fun `étant donné un id de playlist invalide lorsqu'on obtient les chansons de la playlist on obtient une liste vide`() = runTest {
        val playlistId = -1
        Mockito.`when`(mockModèle.obtenirListeDeLectureParId(playlistId)).thenReturn(null)

        val résultat = présentateur.obtenirChansonsDeLaListeDeLecture(playlistId)

        assertNotNull(résultat)
        assertTrue(résultat.isEmpty())
    }

    @Test
    fun `étant donné un id de playlist valide lorsqu'on obtient les détails de la playlist on obtient un objet ListeDeLecture avec un nom valide`() = runTest {
        val playlistId = 2
        val playlist = ListeDeLecture(
            id = playlistId,
            nom = "Playlist Test",
            chansons = mutableListOf()
        )

        Mockito.`when`(mockModèle.obtenirListeDeLectureParId(playlistId)).thenReturn(playlist)

        val résultat = présentateur.obtenirDetailsListeDeLecture(playlistId)

        assertNotNull(résultat)
        assertEquals("Playlist Test", résultat?.nom)
    }

    @Test
    fun `étant donné un id de playlist invalide lorsqu'on obtient les détails de la playlist on obtient null`() = runTest {
        val playlistId = -1
        Mockito.`when`(mockModèle.obtenirListeDeLectureParId(playlistId)).thenReturn(null)

        val résultat = présentateur.obtenirDetailsListeDeLecture(playlistId)

        assertEquals(null, résultat)
    }

    @Test
    fun `étant donné un id d'artiste valide lorsqu'on obtient le pseudo de l'artiste on obtient un pseudo valide`() = runTest {
        val artisteId = 10
        val pseudo = "Artiste Test"
        Mockito.`when`(mockModèle.obtenirTousLesArtistes()).thenReturn(
            listOf(
                com.example.streamifymvp.Domaine.entitees.Artiste(
                    id = artisteId,
                    nom = "Nom",
                    prenom = "Prénom",
                    pseudoArtiste = pseudo,
                    imageArtiste = "image_url"
                )
            )
        )

        val résultat = présentateur.obtenirPseudoArtiste(artisteId)

        assertEquals(pseudo, résultat)
    }

    @Test
    fun `étant donné un id d'artiste invalide lorsqu'on obtient le pseudo de l'artiste on obtient Artiste inconnu`() = runTest {
        val artisteId = 999
        Mockito.`when`(mockModèle.obtenirTousLesArtistes()).thenReturn(emptyList())

        val résultat = présentateur.obtenirPseudoArtiste(artisteId)

        assertEquals("Artiste inconnu", résultat)
    }
}
