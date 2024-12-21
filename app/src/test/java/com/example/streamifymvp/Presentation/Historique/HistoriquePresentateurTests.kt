package com.example.streamifymvp.Presentation.Historique

import com.example.streamifymvp.SourceDeDonnees.ISourceDeDonnee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

class HistoriquePresentateurTests {

    @Mock
    private lateinit var mockVue: ContratVuePrésentateurHistorique.IHistoriqueVue

    @Mock
    private lateinit var mockService: ISourceDeDonnee

    private lateinit var présentateur: HistoriquePrésentateur
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        présentateur = HistoriquePrésentateur(mockVue, mockService, testDispatcher)
    }

    @Test
    fun `étant donné un service avec un historique non vide lorsqu'on charge l'historique, la vue affiche l'historique`() = runTest(testDispatcher) {
        val historique = listOf("Recherche 1", "Recherche 2", "Recherche 3")
        Mockito.`when`(mockService.obtenirHistoriqueRecherche()).thenReturn(historique)

        présentateur.chargerHistoriqueRecherche()

        Mockito.verify(mockVue).afficherHistoriqueRecherche(historique)
    }

    @Test
    fun `étant donné un service avec un historique vide lorsqu'on charge l'historique, la vue affiche un message d'erreur`() = runTest(testDispatcher) {
        Mockito.`when`(mockService.obtenirHistoriqueRecherche()).thenReturn(emptyList())

        présentateur.chargerHistoriqueRecherche()

        Mockito.verify(mockVue).afficherMessageErreur("Aucun historique trouvé.")
    }

    @Test
    fun `étant donné un service qui génère une exception lorsqu'on charge l'historique, la vue affiche un message d'erreur générique`() = runTest(testDispatcher) {
        Mockito.`when`(mockService.obtenirHistoriqueRecherche()).thenThrow(RuntimeException("Erreur de connexion"))

        présentateur.chargerHistoriqueRecherche()

        Mockito.verify(mockVue).afficherMessageErreur("Erreur lors du chargement de l'historique de recherche.")
    }
}
