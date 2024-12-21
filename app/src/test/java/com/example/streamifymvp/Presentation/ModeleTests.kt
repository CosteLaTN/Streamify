package com.example.streamifymvp.Presentation

import com.example.streamifymvp.Domaine.Service.ArtisteService
import com.example.streamifymvp.Domaine.Service.ChansonService
import com.example.streamifymvp.Domaine.Service.ListeDeLectureService
import com.example.streamifymvp.Domaine.entitees.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.internal.util.reflection.*

@OptIn(ExperimentalCoroutinesApi::class)
class ModeleTests {

    private lateinit var modele: Modele


    private lateinit var mockChansonService: ChansonService
    private lateinit var mockArtisteService: ArtisteService
    private lateinit var mockListeDeLectureService: ListeDeLectureService

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)


        modele = Modele()


        mockChansonService = Mockito.mock(ChansonService::class.java)
        mockArtisteService = Mockito.mock(ArtisteService::class.java)
        mockListeDeLectureService = Mockito.mock(ListeDeLectureService::class.java)



    }

    @Test
    fun `etant donne que obtenirToutesLesChansons renvoie des chansons lorsqu'on appelle modele_obtenirToutesLesChansons alors on recupere ces chansons`() = runTest {
        // ARRANGE
        val chansonsSimulees = listOf(
            Chanson(
                id = 1,
                nom = "Song A",
                datePublication = "2020-01-01",
                genre = "Pop",
                dureeAlbum = "40:00",
                dureeMusique = "3:30",
                imageChanson = "urlA",
                fichierAudio = "audioA",
                albumId = null,
                artiste = null
            )
        )
        Mockito.`when`(mockChansonService.obtenirToutesLesChansons()).thenReturn(chansonsSimulees)

        // ACT
        val resultat = modele.obtenirToutesLesChansons()

        // ASSERT
        assertEquals(chansonsSimulees.size, resultat.size)
        assertEquals(chansonsSimulees[0].nom, resultat[0].nom)
        Mockito.verify(mockChansonService).obtenirToutesLesChansons()
    }

    @Test
    fun `etant donne que obtenirNouveautes renvoie une liste vide lorsqu'on appelle modele_obtenirNouveautes alors on recoit une liste vide`() = runTest {
        // ARRANGE
        Mockito.`when`(mockChansonService.obtenirNouveautés()).thenReturn(emptyList())

        // ACT
        val result = modele.obtenirNouveautés()

        // ASSERT
        assertTrue(result.isEmpty())
        Mockito.verify(mockChansonService).obtenirNouveautés()
    }

    @Test
    fun `etant donne un mot cle lorsqu'on appelle modele_rechercherChansons alors on obtient la liste renvoyee par le service`() = runTest {
        // ARRANGE
        val recherche = "Daft"
        val chansonsSimulees = listOf(
            Chanson(2, "One More Time", "2001-03-12", "Electro", "60:00", "5:20", "urlImg", "urlAudio", null, null),
            Chanson(3, "Aerodynamic", "2001-03-12", "Electro", "60:00", "3:30", "urlImg2", "urlAudio2", null, null)
        )
        Mockito.`when`(mockChansonService.rechercherChansons(recherche)).thenReturn(chansonsSimulees)

        // ACT
        val resultat = modele.rechercherChansons(recherche)

        // ASSERT
        assertEquals(chansonsSimulees.size, resultat.size)
        assertEquals("One More Time", resultat[0].nom)
        Mockito.verify(mockChansonService).rechercherChansons(recherche)
    }

    @Test
    fun `etant donne obtenirNouveauxArtistes renvoie des artistes lorsqu'on appelle modele_obtenirNouveauxArtistes alors on obtient ces artistes`() = runTest {
        // ARRANGE
        val artistesSimules = listOf(
            Artiste(id = 1, nom = "Smith", prenom = "John", pseudoArtiste = "John Smith", imageArtiste = "img1"),
            Artiste(id = 2, nom = "Doe", prenom = "Jane", pseudoArtiste = "JaneD", imageArtiste = "img2")
        )
        Mockito.`when`(mockArtisteService.obtenirTousLesArtistes()).thenReturn(artistesSimules)

        // ACT
        val resultat = modele.obtenirNouveauxArtistes()

        // ASSERT
        assertEquals(2, resultat.size)
        assertEquals("John Smith", resultat[0].pseudoArtiste)
        Mockito.verify(mockArtisteService).obtenirTousLesArtistes()
    }

    @Test
    fun `etant donne qu'on ajoute une chanson aux favoris lorsqu'on appelle ajouterChansonAuxFavoris alors le service est invoque`() = runTest {
        // ARRANGE
        val chansonAAjouter = Chanson(42, "TestSong", "2020-01-01", "Rock", "40:00", "3:40", "img", "audio", null, null)

        // ACT
        modele.ajouterChansonAuxFavoris(chansonAAjouter)

        // ASSERT
        Mockito.verify(mockChansonService).ajouterAuxFavoris(chansonAAjouter)
    }

    @Test
    fun `etant donne une playlist Favoris contenant des chansons lorsqu'on appelle obtenirFavoris alors on recupere la playlist`() = runTest {
        // ARRANGE
        val favorisSimules = ListeDeLecture(1, "Favoris", mutableListOf())
        Mockito.`when`(mockListeDeLectureService.obtenirListeDeLectureParNom("Favoris")).thenReturn(favorisSimules)

        // ACT
        val resultat = modele.obtenirFavoris()

        // ASSERT
        assertNotNull(resultat)
        assertEquals("Favoris", resultat!!.nom)
        Mockito.verify(mockListeDeLectureService).obtenirListeDeLectureParNom("Favoris")
    }

    @Test
    fun `etant donne un playlistId existant lorsqu'on appelle obtenirListeDeLectureParId alors on recupere la bonne playlist`() = runTest {
        // ARRANGE
        val playlistId = 99
        val playlistMock = ListeDeLecture(playlistId, "Test Playlist")
        Mockito.`when`(mockListeDeLectureService.obtenirListeDeLectureParId(playlistId)).thenReturn(playlistMock)

        // ACT
        val resultat = modele.obtenirListeDeLectureParId(playlistId)

        // ASSERT
        assertEquals(playlistId, resultat?.id)
        assertEquals("Test Playlist", resultat?.nom)
        Mockito.verify(mockListeDeLectureService).obtenirListeDeLectureParId(playlistId)
    }

    @Test
    fun `etant donne qu'on veut toutes les playlists lorsqu'on appelle obtenirToutesLesListesDeLecture alors on recupere la liste du service`() = runTest {
        // ARRANGE
        val playlistsSimulees = listOf(
            ListeDeLecture(1, "Favoris"),
            ListeDeLecture(2, "Rock legends")
        )
        Mockito.`when`(mockListeDeLectureService.obtenirToutesLesListesDeLecture()).thenReturn(playlistsSimulees)

        // ACT
        val resultat = modele.obtenirToutesLesListesDeLecture()

        // ASSERT
        assertEquals(2, resultat.size)
        assertEquals("Rock legends", resultat[1].nom)
        Mockito.verify(mockListeDeLectureService).obtenirToutesLesListesDeLecture()
    }

    @Test
    fun `etant donne un playlistId et une chanson lorsqu'on appelle ajouterChansonAPlaylist alors le service est invoque`() = runTest {
        // ARRANGE
        val playlistId = 123
        val chansonTest = Chanson(99, "Ma chanson", "2023-01-01", "Pop", "40:00", "3:30", "img", "audio", null, null)

        // ACT
        modele.ajouterChansonAPlaylist(playlistId, chansonTest)

        // ASSERT
        Mockito.verify(mockListeDeLectureService).ajouterChansonAPlaylist(playlistId, chansonTest)
    }

    @Test
    fun `etant donne un playlist en parametre lorsqu'on appelle ajouterPlaylist alors on appelle le service`() = runTest {
        // ARRANGE
        val playlistTest = ListeDeLecture(888, "Nouvelle Playlist")

        // ACT
        modele.ajouterPlaylist(playlistTest)

        // ASSERT
        Mockito.verify(mockListeDeLectureService).ajouterPlaylist(playlistTest)
    }

    @Test
    fun `etant donne un playlistId lorsqu'on appelle obtenirChansonsDeLaListeDeLecture alors on obtient la liste du service`() = runTest {
        // ARRANGE
        val playlistId = 10
        val chansonsSimulees = listOf(
            Chanson(1, "C1", "2021-01-01", "Genre1", "40:00", "3:20", "imgC1", "audioC1", null, null),
            Chanson(2, "C2", "2022-01-01", "Genre2", "40:00", "3:50", "imgC2", "audioC2", null, null),
        )
        Mockito.`when`(mockListeDeLectureService.obtenirChansonsDeLaListeDeLecture(playlistId)).thenReturn(chansonsSimulees)

        // ACT
        val resultat = modele.obtenirChansonsDeLaListeDeLecture(playlistId)

        // ASSERT
        assertEquals(chansonsSimulees.size, resultat.size)
        assertEquals("C2", resultat[1].nom)
        Mockito.verify(mockListeDeLectureService).obtenirChansonsDeLaListeDeLecture(playlistId)
    }
}
