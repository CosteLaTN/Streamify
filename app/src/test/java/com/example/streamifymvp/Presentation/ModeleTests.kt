package com.example.streamifymvp.Presentation

import com.example.streamifymvp.Domaine.entitees.Artiste
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import com.example.streamifymvp.Domaine.Service.ArtisteService
import com.example.streamifymvp.Domaine.Service.ChansonService
import com.example.streamifymvp.Domaine.Service.ListeDeLectureService
import com.example.streamifymvp.R
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ModeleTests {

    private lateinit var mockChansonService: ChansonService
    private lateinit var mockArtisteService: ArtisteService
    private lateinit var mockListeDeLectureService: ListeDeLectureService

    private lateinit var modele: Modele

    @BeforeTest
    fun setup() {
        mockChansonService = mockk()
        mockArtisteService = mockk()
        mockListeDeLectureService = mockk()

        modele = Modele(mockChansonService, mockArtisteService, mockListeDeLectureService)
    }

    @Test
    fun `etant donne le Modele lorsqu'on appelle obtenirToutesLesChansons alors on obtient la liste de chansons du service`() {
        // Arrange
        val chansons = listOf(
            Chanson(
                id = 1,
                nom = "Dont Cry",
                datePublication = "2019-09-17",
                genre = "Rock",
                dureeAlbum = "45:00",
                dureeMusique = "4:44",
                imageChanson = 0,
                fichierAudio = 0,
                albumId = 1,
                artisteId = 1
            )
        )
        every { mockChansonService.obtenirToutesLesChansons() } returns chansons

        // Act
        val resultat = modele.obtenirToutesLesChansons()

        // Assert
        assertEquals(chansons, resultat)
        verify { mockChansonService.obtenirToutesLesChansons() }
    }

    @Test
    fun `etant donne le Modele lorsqu'on appelle obtenirNouveautes alors on obtient la liste des nouveautes du service`() {
        // Arrange
        val nouveautes = listOf(
            Chanson(
                id = 2,
                nom = "New Song",
                datePublication = "2022-01-01",
                genre = "Pop",
                dureeAlbum = "40:00",
                dureeMusique = "3:33",
                imageChanson = 0,
                fichierAudio = 0,
                albumId = 1,
                artisteId = 1
            )
        )
        every { mockChansonService.obtenirNouveautés() } returns nouveautes

        // Act
        val resultat = modele.obtenirNouveautés()

        // Assert
        assertEquals(nouveautes, resultat)
        verify { mockChansonService.obtenirNouveautés() }
    }

    @Test
    fun `etant donne une recherche dans le Modele lorsqu'on appelle rechercherChansons alors on obtient les chansons correspondantes`() {
        // Arrange
        val recherche = "Dont Cry"
        val chansons = listOf(
            Chanson(
                id = 1,
                nom = "Dont Cry",
                datePublication = "2019-09-17",
                genre = "Rock",
                dureeAlbum = "45:00",
                dureeMusique = "4:44",
                imageChanson = 0,
                fichierAudio = 0,
                albumId = 1,
                artisteId = 1
            )
        )
        every { mockChansonService.rechercherChansons(recherche) } returns chansons

        // Act
        val resultat = modele.rechercherChansons(recherche)

        // Assert
        assertEquals(chansons, resultat)
        verify { mockChansonService.rechercherChansons(recherche) }
    }

    @Test
    fun `etant donne le Modele lorsqu'on appelle obtenirNouveauxArtistes alors on obtient la liste des artistes`() {
        // Arrange
        val artistes = listOf(
            Artiste(
                id = 1,
                prenom = "Axl",
                nom = "Rose",
                pseudoArtiste = "Guns N' Roses",
                imageArtiste = R.drawable.cry
            )
        )
        every { mockArtisteService.obtenirTousLesArtistes() } returns artistes

        // Act
        val resultat = modele.obtenirNouveauxArtistes()

        // Assert
        assertEquals(artistes, resultat)
        verify { mockArtisteService.obtenirTousLesArtistes() }
    }

    @Test
    fun `etant donne une chanson lorsqu'on appelle ajouterChansonAuxFavoris alors chansonService ajoute la chanson aux favoris`() {
        // Arrange
        val chanson = Chanson(
            id = 1,
            nom = "Dont Cry",
            datePublication = "2019-09-17",
            genre = "Rock",
            dureeAlbum = "45:00",
            dureeMusique = "4:44",
            imageChanson = 0,
            fichierAudio = 0,
            albumId = 1,
            artisteId = 1
        )

        // Stub la méthode ajouterAuxFavoris pour que MockK sache qu'elle peut être appelée
        every { mockChansonService.ajouterAuxFavoris(chanson) } just Runs

        // Act
        modele.ajouterChansonAuxFavoris(chanson)

        // Assert
        verify { mockChansonService.ajouterAuxFavoris(chanson) }
    }


    @Test
    fun `etant donne le Modele lorsqu'on appelle obtenirFavoris alors on obtient la liste de lecture favoris`() = runBlocking {
        // Arrange
        val favoris = ListeDeLecture(
            id = 1,
            nom = "Favoris",
            chansons = mutableListOf(
                Chanson(
                    id = 1,
                    nom = "Dont Cry",
                    datePublication = "2019-09-17",
                    genre = "Rock",
                    dureeAlbum = "45:00",
                    dureeMusique = "4:44",
                    imageChanson = R.drawable.cry,
                    fichierAudio = R.raw.dontcry,
                    albumId = 1,
                    artisteId = 1
                )
            )
        )
        coEvery { mockListeDeLectureService.obtenirListeDeLectureParNom("Favoris") } returns favoris

        // Act
        val resultat = modele.obtenirFavoris()

        // Assert
        assertEquals(favoris, resultat)
        coVerify { mockListeDeLectureService.obtenirListeDeLectureParNom("Favoris") }
    }

    @Test
    fun `etant donne un id de playlist lorsqu'on appelle obtenirListeDeLectureParId alors on obtient la playlist correspondante`() {
        // Arrange
        val playlistId = 1
        val playlist = ListeDeLecture(
            id = playlistId,
            nom = "Rock Classics",
            chansons = mutableListOf(
                Chanson(
                    id = 1,
                    nom = "Bohemian Rhapsody",
                    datePublication = "1975-10-31",
                    genre = "Rock",
                    dureeAlbum = "5:55",
                    dureeMusique = "5:55",
                    imageChanson = 0,
                    fichierAudio = 0,
                    albumId = 1,
                    artisteId = 1
                )
            )
        )
        every { mockListeDeLectureService.obtenirListeDeLectureParId(playlistId) } returns playlist

        // Act
        val resultat = modele.obtenirListeDeLectureParId(playlistId)

        // Assert
        assertEquals(playlist, resultat)
        verify { mockListeDeLectureService.obtenirListeDeLectureParId(playlistId) }
    }

    @Test
    fun `etant donne le Modele lorsqu'on appelle obtenirTousLesArtistes alors on obtient la liste des artistes du service`() {
        // Arrange
        val artistes = listOf(
            Artiste(
                id = 2,
                prenom = "will.i.am",
                nom = "",
                pseudoArtiste = "The Black Eyed Peas",
                imageArtiste = R.drawable.humps
            )
        )
        every { mockArtisteService.obtenirTousLesArtistes() } returns artistes

        // Act
        val resultat = modele.obtenirTousLesArtistes()

        // Assert
        assertEquals(artistes, resultat)
        verify { mockArtisteService.obtenirTousLesArtistes() }
    }
}
