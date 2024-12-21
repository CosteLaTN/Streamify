package com.example.streamifymvp.Presentation.showDates

import com.example.streamifymvp.Domaine.entitees.ShowDate
import com.example.streamifymvp.SourceDeDonnees.SourceDeDonneeHTTP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ShowDatesPresentateurTests {

    @Mock
    private lateinit var mockVue: ContratVuePrésentateurShowDates.IShowDatesVue

    @Mock
    private lateinit var mockService: SourceDeDonneeHTTP

    private lateinit var présentateur: ShowDatesPrésentateur
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        présentateur = ShowDatesPrésentateur(mockVue, mockService)
    }

    @Test
    fun `étant donné des dates disponibles, lorsqu'on charge les dates, elles sont affichées`() = runTest(testDispatcher) {
        val mockDates = listOf(
            ShowDate(title = "Concert 1", details = "Détails du concert 1", location = "Lieu 1", date = java.util.Date()),
            ShowDate(title = "Concert 2", details = "Détails du concert 2", location = "Lieu 2", date = java.util.Date())
        )
        Mockito.`when`(mockService.obtenirToutesLesDatesDeShow()).thenReturn(mockDates)

        présentateur.chargerDates()

        Mockito.verify(mockVue).afficherDates(mockDates)
    }

    @Test
    fun `étant donné aucune date disponible, lorsqu'on charge les dates, un message d'erreur est affiché`() = runTest(testDispatcher) {
        Mockito.`when`(mockService.obtenirToutesLesDatesDeShow()).thenReturn(emptyList())

        présentateur.chargerDates()

        Mockito.verify(mockVue).afficherMessageErreur("Aucune date disponible.")
    }

    @Test
    fun `étant donné une erreur lors du chargement des dates, lorsqu'on charge les dates, un message d'erreur est affiché`() = runTest(testDispatcher) {
        Mockito.`when`(mockService.obtenirToutesLesDatesDeShow()).thenThrow(RuntimeException("Erreur réseau"))

        présentateur.chargerDates()

        Mockito.verify(mockVue).afficherMessageErreur("Erreur lors du chargement des dates")
    }
}
