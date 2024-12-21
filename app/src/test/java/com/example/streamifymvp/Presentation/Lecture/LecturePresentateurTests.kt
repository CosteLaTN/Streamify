package com.example.streamifymvp.Presentation.Lecture

import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import com.example.streamifymvp.Presentation.Modele
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LecturePresentateurTests {

    @Mock
    private lateinit var mockModele: Modele

    private lateinit var présentateur: LecturePresentateur
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        présentateur = LecturePresentateur(mockModele, testDispatcher)
    }

    @Test
    fun `étant donné une chanson existante lorsqu'on la récupère par ID, la chanson correspondante est retournée`() = runTest(testDispatcher) {
        val chansonId = 1
        val chanson = Chanson(
            id = chansonId,
            nom = "Test Song",
            datePublication = "2024-01-01",
            genre = "Pop",
            dureeAlbum = "3:30",
            dureeMusique = "3:30",
            imageChanson = "test_image.jpg",
            fichierAudio = "test_audio.mp3",
            albumId = null,
            artiste = null
        )
        Mockito.`when`(mockModele.obtenirToutesLesChansons()).thenReturn(listOf(chanson))

        val result = présentateur.obtenirChansonParId(chansonId)

        assertEquals(chanson, result)
    }

    @Test
    fun `étant donné une chanson inexistante lorsqu'on tente de la récupérer par ID, une exception est levée`() = runTest(testDispatcher) {
        Mockito.`when`(mockModele.obtenirToutesLesChansons()).thenReturn(emptyList())

        assertFailsWith<IllegalArgumentException>("Chanson introuvable") {
            présentateur.obtenirChansonParId(999)
        }
    }

    @Test
    fun `étant donné un genre lorsqu'on récupère les chansons correspondantes, une liste filtrée est retournée`() = runTest(testDispatcher) {
        val chansons = listOf(
            Chanson(1, "Song 1", "2024-01-01", "Pop", "3:30", "3:30", "image1.jpg", "audio1.mp3", null, null),
            Chanson(2, "Song 2", "2024-01-01", "Rock", "4:00", "4:00", "image2.jpg", "audio2.mp3", null, null)
        )
        Mockito.`when`(mockModele.obtenirToutesLesChansons()).thenReturn(chansons)

        val result = présentateur.obtenirChansonsParGenre("Pop")

        assertEquals(listOf(chansons[0]), result)
    }

    @Test
    fun `étant donné une chanson lorsqu'on l'ajoute aux favoris, le modèle est appelé avec succès`() = runTest(testDispatcher) {
        val chanson = Chanson(
            id = 1,
            nom = "Favorite Song",
            datePublication = "2024-01-01",
            genre = "Pop",
            dureeAlbum = "3:30",
            dureeMusique = "3:30",
            imageChanson = "test_image.jpg",
            fichierAudio = "test_audio.mp3",
            albumId = null,
            artiste = null
        )

        présentateur.ajouterAuxFavoris(chanson)

        Mockito.verify(mockModele).ajouterChansonAuxFavoris(chanson)
    }

    @Test
    fun `étant donné des listes de lecture lorsqu'on les récupère, le modèle retourne les listes correctement`() = runTest(testDispatcher) {
        val playlists = listOf(
            ListeDeLecture(1, "Playlist 1", mutableListOf()),
            ListeDeLecture(2, "Playlist 2", mutableListOf())
        )
        Mockito.`when`(mockModele.obtenirToutesLesListesDeLecture()).thenReturn(playlists)

        val result = présentateur.obtenirToutesLesListesDeLecture()

        assertEquals(playlists, result)
    }

    @Test
    fun `étant donné une playlist et une chanson lorsqu'on ajoute la chanson à la playlist, l'ajout est effectué correctement`() = runTest(testDispatcher) {
        val playlistId = 1
        val chanson = Chanson(
            id = 1,
            nom = "New Song",
            datePublication = "2024-01-01",
            genre = "Pop",
            dureeAlbum = "3:30",
            dureeMusique = "3:30",
            imageChanson = "test_image.jpg",
            fichierAudio = "test_audio.mp3",
            albumId = null,
            artiste = null
        )

        présentateur.ajouterChansonAPlaylist(playlistId, chanson)

        Mockito.verify(mockModele).ajouterChansonAPlaylist(playlistId, chanson)
    }
}
