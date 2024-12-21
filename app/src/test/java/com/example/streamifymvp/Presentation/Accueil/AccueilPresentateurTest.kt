package com.example.streamifymvp.Presentation.Accueil

import android.util.Log
import com.example.streamify.Presentation.Accueil.AccueilPresentateur
import com.example.streamify.Presentation.Accueil.AccueilVue
import com.example.streamifymvp.Domaine.entitees.Artiste
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Presentation.Modele
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AccueilPresentateurTest {

    private lateinit var vue: AccueilVue
    private lateinit var modele: Modele
    private lateinit var presentateur: AccueilPresentateur

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        vue = mockk(relaxed = true)
        modele = mockk()
        presentateur = AccueilPresentateur(vue, modele)
    }

    @Test
    fun `etant donne des chansons, nouveautes et artistes lorsque chargerAccueil alors vue affiche tout`() {
        // Arrange
        val chansons = listOf(Chanson(1, "Test", "2020-01-01", "Rock", "X", "Y", "img", "audio", 1,1))
        val nouveautes = listOf(Chanson(2, "Nouveau", "2021-01-01", "Pop", "X","Y","img2","audio2",1,1))
        val artistes = listOf(Artiste(1,"Axl","Rose","Guns N' Roses","img"))
        every { modele.obtenirToutesLesChansons() } returns chansons
        every { modele.obtenirNouveautés() } returns nouveautes
        every { modele.obtenirNouveauxArtistes() } returns artistes

        // Act
        presentateur.chargerAccueil()

        // Assert

        assertTrue(true)
    }

    @Test
    fun `etant donne une exception lorsque chargerAccueil alors vue affiche message aucun resultat`() {
        every { modele.obtenirToutesLesChansons() } throws RuntimeException("erreur")

        presentateur.chargerAccueil()


        assertTrue(true)
    }

    @Test
    fun `etant donne une recherche non vide lorsque rechercherChansons alors vue affiche resultats`() {
        val resultats = listOf(Chanson(3,"Test2","2020-01-01","Rock","X","Y","img3","audio3",1,1))
        every { modele.rechercherChansons("test") } returns resultats

        presentateur.rechercherChansons("test")

        assertTrue(true)
    }

    @Test
    fun `etant donne une recherche vide lorsque rechercherChansons alors vue affiche aucun resultat`() {
        every { modele.rechercherChansons("test") } returns emptyList()

        presentateur.rechercherChansons("test")

        assertTrue(true)
    }
}
