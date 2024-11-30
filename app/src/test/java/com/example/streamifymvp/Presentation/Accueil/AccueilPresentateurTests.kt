package com.example.streamifymvp.Presentation.Accueil

import com.example.streamify.Presentation.Accueil.AccueilPresentateur
import com.example.streamify.Presentation.Accueil.AccueilVue
import com.example.streamifymvp.Domaine.entitees.Artiste
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Presentation.Modele
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class AccueilPresentateurTests {

    private lateinit var vue: AccueilVue
    private lateinit var modele: Modele
    private lateinit var presentateur: AccueilPresentateur

    @Before
    fun setup() {
        vue = mockk(relaxed = true)
        modele = mockk()
        presentateur = AccueilPresentateur(vue, modele)
    }

    @Test
    fun `etant donne l'accueil lorsqu'on appelle chargerAccueil alors les chansons, nouveautes et artistes sont affiches`() {
        // Arrange
        val chansons = listOf(Chanson(1, "Song 1", "2022-01-01", "Pop", "3:45", "3:45", 0, 0, 1, 1))
        val nouveautes = listOf(Chanson(2, "New Song", "2022-02-01", "Rock", "4:00", "4:00", 0, 0, 2, 2))
        val artistes = listOf(Artiste(1, "Artist 1", "Last", "Stage Name", 0))

        every { modele.obtenirToutesLesChansons() } returns chansons
        every { modele.obtenirNouveautés() } returns nouveautes
        every { modele.obtenirNouveauxArtistes() } returns artistes

        // Act
        presentateur.chargerAccueil()

        // Assert
        verify { vue.afficherChansons(chansons) }
        verify { vue.afficherNouveautés(nouveautes) }
        verify { vue.afficherNouveauxArtistes(artistes) }
    }

    @Test
    fun `etant donne une recherche lorsqu'on appelle rechercherChansons sans resultat alors afficher message aucun resultat`() {
        // Arrange
        val recherche = "Unknown Song"
        every { modele.rechercherChansons(recherche) } returns emptyList()

        // Act
        presentateur.rechercherChansons(recherche)

        // Assert
        verify { vue.afficherMessageAucunRésultat() }
    }
}


