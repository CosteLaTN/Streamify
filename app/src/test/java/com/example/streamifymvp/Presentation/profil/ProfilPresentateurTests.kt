package com.example.streamifymvp.Presentation.profil

import com.example.streamifymvp.Presentation.profil.ProfilPrésentateur
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfilPresentateurTests {

    private lateinit var vue: ContratVuePrésentateurProfil.IProfilVue
    private lateinit var service: SourceDeDonneeBidon
    private lateinit var presentateur: ProfilPrésentateur
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        vue = mockk(relaxed = true)
        service = mockk()
        presentateur = ProfilPrésentateur(vue, testDispatcher, service)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `etant donne un profil null lorsqu'on appelle chargerProfil alors afficher message d'erreur`() = runTest {

        coEvery { service.obtenirProfil() } returns null

        // Act
        presentateur.chargerProfil()

        // Assert : Vérifier que la méthode afficherMessageErreur a été appelée avec le bon message
        coVerify { vue.afficherMessageErreur("Erreur lors du chargement du profil") }
    }

    @Test
    fun `etant donne une exception lorsqu'on appelle chargerProfil alors afficher message d'erreur`() = runTest {
        // Arrange : Configurer le mock pour lancer une exception
        coEvery { service.obtenirProfil() } throws RuntimeException("Erreur simulée")

        // Act
        presentateur.chargerProfil()

        // Assert : Vérifier que la méthode afficherMessageErreur a été appelée avec le bon message
        coVerify { vue.afficherMessageErreur("Erreur lors du chargement du profil") }
    }
}
