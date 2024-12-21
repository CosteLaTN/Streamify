package com.example.streamifymvp.Presentation.Recherche

import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Presentation.IModele
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class RecherchePresentateurTest {

    @Mock
    private lateinit var mockVue: RechercheContrat.IRechercheVue

    @Mock
    private lateinit var mockModele: IModele

    private lateinit var présentateur: RecherchePresentateur
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        présentateur = RecherchePresentateur(mockModele, mockVue)
    }

    @Test
    fun `étant donné une recherche valide, lorsqu'on effectue une recherche, les résultats sont affichés`() = runTest(testDispatcher) {
        val query = "Imagine"
        val chansons = listOf(
            Chanson(
                id = 1,
                nom = "Imagine",
                datePublication = "1971",
                genre = "Pop",
                dureeAlbum = "3:00",
                dureeMusique = "3:00",
                imageChanson = "imageUrl",
                fichierAudio = "audioUrl",
                albumId = 1,
                artiste = null
            )
        )
        Mockito.`when`(mockModele.rechercherChansons(query)).thenReturn(chansons)

        présentateur.effectuerRecherche(query)

        Mockito.verify(mockVue).afficherResultats(chansons)
    }

    @Test
    fun `étant donné une recherche sans résultats, lorsqu'on effectue une recherche, un message d'aucun résultat est affiché`() = runTest(testDispatcher) {
        val query = "Unknown Song"
        Mockito.`when`(mockModele.rechercherChansons(query)).thenReturn(emptyList())

        présentateur.effectuerRecherche(query)

        Mockito.verify(mockVue).afficherMessageAucunResultat()
    }

    @Test
    fun `étant donné une erreur lors de la recherche, lorsqu'on effectue une recherche, un message d'aucun résultat est affiché`() = runTest(testDispatcher) {
        val query = "ErrorSong"
        Mockito.`when`(mockModele.rechercherChansons(query)).thenThrow(RuntimeException("Service Error"))

        présentateur.effectuerRecherche(query)

        Mockito.verify(mockVue).afficherMessageAucunResultat()
    }
}
