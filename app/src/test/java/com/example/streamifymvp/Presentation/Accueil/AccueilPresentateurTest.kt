package com.example.streamify.Presentation.Accueil

import com.example.streamifymvp.Domaine.entitees.Artiste
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Presentation.Accueil.IAccueilPresentateur
import com.example.streamifymvp.Presentation.Modele
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import com.example.streamify.Presentation.Accueil.AccueilVue
import kotlin.test.* // @BeforeTest, @Test, assertEquals, etc.
import kotlinx.coroutines.test.runTest
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.eq
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Exemple de test pour AccueilPresentateur, inspiré de la nomenclature du test TéléchargementServiceTests.
 */
class AccueilPresentateurTest {


    private lateinit var vue: AccueilVue
    private lateinit var modele: Modele
    private lateinit var presentateur: AccueilPresentateur

    @BeforeTest
    fun initMocks() {

        vue = Mockito.mock(AccueilVue::class.java)
        modele = Mockito.mock(Modele::class.java)


        presentateur = AccueilPresentateur(vue, modele)
    }

    @Test
    fun `etant donne un AccueilPresentateur lorsque je charge l'accueil alors on affiche chansons, nouveautes et artistes`() = runTest {
        // ARRANGE
        val chansonsSimulees = listOf(
            Chanson(
                id = 1,
                nom = "Song 1",
                datePublication = "2023-01-01",
                genre = "Pop",
                dureeAlbum = "45:00",
                dureeMusique = "3:30",
                imageChanson = "http://test.com/img.jpg",
                fichierAudio = "http://test.com/audio.mp3",
                albumId = null,
                artiste = Artiste(
                    id = 11,
                    nom = "Toto",
                    prenom = "Titi",
                    pseudoArtiste = "TotoTiti",
                    imageArtiste = "http://test.com/art.png",

                )
            )
        )
        val nouveautesSimulees = listOf(
            Chanson(
                id = 2,
                nom = "Song 2",
                datePublication = "2023-07-10",
                genre = "Rock",
                dureeAlbum = "40:00",
                dureeMusique = "4:00",
                imageChanson = "http://test.com/img2.jpg",
                fichierAudio = "http://test.com/audio2.mp3",
                albumId = null,
                artiste = null
            )
        )
        val artistesSimules = listOf(
            Artiste(
                id = 100,
                nom = "Test",
                prenom = "John",
                pseudoArtiste = "JohnTest",
                imageArtiste = "http://test.com/artist.jpg"
            )
        )

        whenever(modele.obtenirToutesLesChansons()).thenReturn(chansonsSimulees)
        whenever(modele.obtenirNouveautés()).thenReturn(nouveautesSimulees)
        whenever(modele.obtenirNouveauxArtistes()).thenReturn(artistesSimules)

        // ACT
        presentateur.chargerAccueil()

        // ASSERT
        verify(vue).afficherChansons(chansonsSimulees)
        verify(vue).afficherNouveautés(nouveautesSimulees)
        verify(vue).afficherNouveauxArtistes(artistesSimules)


        verify(vue, never()).afficherMessageAucunRésultat()
    }

    @Test
    fun `etant donne un AccueilPresentateur lorsque je charge l'accueil et qu'une exception survient alors on affiche message aucun resultat`() = runTest {
        // ARRANGE
        whenever(modele.obtenirToutesLesChansons()).thenThrow(RuntimeException("Erreur"))

        // ACT
        presentateur.chargerAccueil()

        // ASSERT
        verify(vue).afficherMessageAucunRésultat()
        verify(vue, never()).afficherChansons(any())
        verify(vue, never()).afficherNouveautés(any())
        verify(vue, never()).afficherNouveauxArtistes(any())
    }

    @Test
    fun `etant donne un AccueilPresentateur et une recherche non-vide lorsqu'on appelle rechercherChansons et que des resultats existent alors on affiche ces chansons`() = runTest {
        // ARRANGE
        val recherche = "Daft Punk"
        val resultats = listOf(
            Chanson(
                id = 3,
                nom = "One More Time",
                datePublication = "2001-03-12",
                genre = "Electronique",
                dureeAlbum = "60:00",
                dureeMusique = "3:30",
                imageChanson = "http://test.com/daft.jpg",
                fichierAudio = "http://test.com/daft.mp3",
                albumId = null,
                artiste = null,


            )
        )
        whenever(modele.rechercherChansons(eq(recherche))).thenReturn(resultats)

        // ACT
        presentateur.rechercherChansons(recherche)

        // ASSERT
        verify(vue).afficherChansons(resultats)
        verify(vue, never()).afficherMessageAucunRésultat()
    }

    @Test
    fun `etant donne un AccueilPresentateur et une recherche non-vide lorsqu'on appelle rechercherChansons et que aucun resultat alors on affiche message aucun resultat`() = runTest {
        // ARRANGE
        val recherche = "Inexistant"
        whenever(modele.rechercherChansons(eq(recherche))).thenReturn(emptyList())

        // ACT
        presentateur.rechercherChansons(recherche)

        // ASSERT
        verify(vue).afficherMessageAucunRésultat()
        verify(vue, never()).afficherChansons(any())
    }

    @Test
    fun `etant donne un AccueilPresentateur et une recherche lorsqu'on appelle rechercherChansons et qu'une exception survient alors on affiche message aucun resultat`() = runTest {
        // ARRANGE
        val recherche = "CrashTest"
        whenever(modele.rechercherChansons(eq(recherche))).thenThrow(RuntimeException("Erreur interne"))

        // ACT
        presentateur.rechercherChansons(recherche)

        // ASSERT
        verify(vue).afficherMessageAucunRésultat()
        verify(vue, never()).afficherChansons(any())
    }
}
