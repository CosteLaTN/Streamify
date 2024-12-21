package com.example.streamifymvp.Presentation.showDates


import com.example.streamifymvp.Domaine.entitees.ShowDate
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import io.mockk.slot



@OptIn(ExperimentalCoroutinesApi::class)
class ShowDatesPresentateurTests {

    private lateinit var vue: ContratVuePrésentateurShowDates.IShowDatesVue
    private lateinit var presentateur: ShowDatesPrésentateur
    private lateinit var service: SourceDeDonneeBidon
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        vue = mockk(relaxed = true)
        service = mockk()
        presentateur = ShowDatesPrésentateur(vue, service)


        coEvery { service.obtenirToutesLesDatesDeShow() } returns listOf(
            ShowDate("Concert de Daft Punk", "Un concert légendaire de Daft Punk", mockk(), "Paris, France")
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `etant donne des dates lorsqu'on appelle chargerDates alors afficher les dates si elles existent`() = runTest {
        // Arrange
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse("2023-11-23") ?: Date()
        val expectedDates = listOf(
            ShowDate("Concert de Daft Punk", "Un concert légendaire de Daft Punk", date, "Paris, France")
        )


        coEvery { service.obtenirToutesLesDatesDeShow() } returns expectedDates

        // Act
        presentateur.chargerDates()
        advanceUntilIdle()

        // Assert
        val slot = slot<List<ShowDate>>()
        coVerify { vue.afficherDates(capture(slot)) }


        val capturedDates = slot.captured
        assertEquals(expectedDates.size, capturedDates.size)
        assertEquals(expectedDates[0].title, capturedDates[0].title)
        assertEquals(expectedDates[0].details, capturedDates[0].details)
        assertEquals(expectedDates[0].location, capturedDates[0].location)


        val expectedDateString = dateFormat.format(expectedDates[0].date)
        val capturedDateString = dateFormat.format(capturedDates[0].date)
        assertEquals(expectedDateString, capturedDateString)
    }




}
