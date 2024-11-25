package com.example.streamifymvp.Presentation.Historique

import HistoriquePrésentateur
import com.example.streamifymvp.SourceDeDonnees.ISourceDeDonnee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class HistoriquePresentateurTests {

    @Mock
    private lateinit var vue: ContratVuePrésentateurHistorique.IHistoriqueVue
    @Mock
    private lateinit var service: ISourceDeDonnee

    private lateinit var presentateur: HistoriquePrésentateur

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        Dispatchers.setMain(testDispatcher)
        presentateur = HistoriquePrésentateur(vue, service, testDispatcher) // Passer l'instance du dispatcher mocké
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Réinitialiser le Dispatcher principal après chaque test
    }

    @Test
    fun `etant donne l'historique lorsqu'on appelle chargerHistoriqueRecherche alors afficher l'historique s'il existe`() = runTest {
        // Arrange
        val historique = listOf("Recherche 1", "Recherche 2")
        `when`(service.obtenirHistoriqueRecherche()).thenReturn(historique)

        // Act
        presentateur.chargerHistoriqueRecherche()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        verify(vue).afficherHistoriqueRecherche(historique)
    }

    @Test
    fun `etant donne l'absence d'historique lorsqu'on appelle chargerHistoriqueRecherche alors afficher message d'erreur`() = runTest {
        // Arrange
        val historique = emptyList<String>()
        `when`(service.obtenirHistoriqueRecherche()).thenReturn(historique)

        // Act
        presentateur.chargerHistoriqueRecherche()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        verify(vue).afficherMessageErreur("Aucun historique trouvé.")
    }

    @Test
    fun `etant donne une exception lorsqu'on appelle chargerHistoriqueRecherche alors afficher message d'erreur`() = runTest {
        // Arrange
        `when`(service.obtenirHistoriqueRecherche()).thenThrow(RuntimeException("Erreur simulée"))

        // Act
        presentateur.chargerHistoriqueRecherche()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        verify(vue).afficherMessageErreur("Erreur lors du chargement de l'historique de recherche")
    }
}
