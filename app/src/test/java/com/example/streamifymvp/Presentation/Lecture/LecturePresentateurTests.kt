package com.example.streamifymvp.Presentation.Lecture

import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import com.example.streamifymvp.Presentation.Modele
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LecturePresentateurTests {

    private lateinit var modele: Modele
    private lateinit var presentateur: LecturePresentateur

    // Utiliser un TestDispatcher pour remplacer le Dispatcher par défaut pendant les tests
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        modele = mockk()
        presentateur = LecturePresentateur(modele, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Réinitialiser le Dispatcher principal après chaque test
    }

    @Test
    fun `etant donne le presentateur lorsqu'on appelle obtenirFavoris alors retourner la liste des favoris`() = runTest {
        // Arrange
        val favoris = ListeDeLecture(1, "Favoris", mutableListOf())
        coEvery { modele.obtenirFavoris() } returns favoris

        // Act
        var result: ListeDeLecture? = null
        presentateur.obtenirFavoris { result = it }
        testDispatcher.scheduler.advanceUntilIdle() // Avancer les coroutines jusqu'à ce qu'elles soient terminées

        // Assert
        assertEquals(favoris, result)
    }
}
