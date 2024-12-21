package com.example.streamifymvp.Presentation.profil

import com.example.streamifymvp.Domaine.entitees.Profil
import com.example.streamifymvp.SourceDeDonnees.ISourceDeDonnee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.assertFailsWith

class ProfilPresentateurTests {

    @Mock
    private lateinit var mockVue: ContratVuePrésentateurProfil.IProfilVue

    @Mock
    private lateinit var mockService: ISourceDeDonnee

    private lateinit var présentateur: ProfilPrésentateur
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        présentateur = ProfilPrésentateur(mockVue, mockService, testDispatcher)
    }

    @Test
    fun `étant donné un profil valide lorsqu'on charge le profil, les données sont affichées dans la vue`() = runTest(testDispatcher) {
        val profil = Profil("NoName", "Name")
        Mockito.`when`(mockService.obtenirProfil()).thenReturn(profil)

        présentateur.chargerProfil()

        Mockito.verify(mockVue).afficherNomUtilisateur(profil.nom)
        Mockito.verify(mockVue).afficherUsername(profil.username)
    }

    @Test
    fun `étant donné un profil invalide lorsqu'on charge le profil, un message d'erreur est affiché`() = runTest(testDispatcher) {
        Mockito.`when`(mockService.obtenirProfil()).thenReturn(null)

        présentateur.chargerProfil()

        Mockito.verify(mockVue).afficherMessageErreur("Erreur lors du chargement du profil.")
    }

    @Test
    fun `étant donné un nouveau nom valide lorsqu'on modifie le nom, le nom est mis à jour dans la vue`() = runTest(testDispatcher) {
        val nouveauNom = "Jane Doe"
        Mockito.`when`(mockService.modifierNomUtilisateur(nouveauNom)).thenReturn(true)

        présentateur.modifierNomUtilisateur(nouveauNom)

        Mockito.verify(mockVue).afficherNomUtilisateur(nouveauNom)
        Mockito.verify(mockVue).afficherMessageSucces("Nom modifié avec succès.")
    }

    @Test
    fun `étant donné un nouveau nom invalide lorsqu'on tente de modifier le nom, un message d'erreur est affiché`() = runTest(testDispatcher) {
        val nouveauNom = ""

        présentateur.modifierNomUtilisateur(nouveauNom)

        Mockito.verify(mockVue).afficherMessageErreur("Le nom ne peut pas être vide.")
    }

    @Test
    fun `étant donné une erreur du service lors de la modification du nom, un message d'erreur est affiché`() = runTest(testDispatcher) {
        val nouveauNom = "Jane Doe"
        Mockito.`when`(mockService.modifierNomUtilisateur(nouveauNom)).thenThrow(RuntimeException("Service error"))

        présentateur.modifierNomUtilisateur(nouveauNom)

        Mockito.verify(mockVue).afficherMessageErreur("Erreur lors de la modification du nom.")
    }

    @Test
    fun `étant donné un nouveau nom d'utilisateur valide lorsqu'on modifie le nom d'utilisateur, le nom d'utilisateur est mis à jour dans la vue`() = runTest(testDispatcher) {
        val nouveauUsername = "janedoe"
        Mockito.`when`(mockService.modifierUsername(nouveauUsername)).thenReturn(true)

        présentateur.modifierUsername(nouveauUsername)

        Mockito.verify(mockVue).afficherUsername(nouveauUsername)
        Mockito.verify(mockVue).afficherMessageSucces("Nom d'utilisateur modifié avec succès.")
    }

    @Test
    fun `étant donné un nouveau nom d'utilisateur invalide lorsqu'on tente de le modifier, un message d'erreur est affiché`() = runTest(testDispatcher) {
        val nouveauUsername = ""

        présentateur.modifierUsername(nouveauUsername)

        Mockito.verify(mockVue).afficherMessageErreur("Le nom d'utilisateur ne peut pas être vide.")
    }

    @Test
    fun `étant donné une erreur du service lors de la modification du nom d'utilisateur, un message d'erreur est affiché`() = runTest(testDispatcher) {
        val nouveauUsername = "janedoe"
        Mockito.`when`(mockService.modifierUsername(nouveauUsername)).thenThrow(RuntimeException("Service error"))

        présentateur.modifierUsername(nouveauUsername)

        Mockito.verify(mockVue).afficherMessageErreur("Erreur lors de la modification du nom d'utilisateur.")
    }

    @Test
    fun `étant donné une chanson valide lorsqu'on sauvegarde la chanson du jour, un message de succès est affiché`() {
        val chanson = "Song of the Day"

        présentateur.sauvegarderChansonDuJour(chanson)

        Mockito.verify(mockVue).afficherMessageSucces("Chanson du jour sauvegardée : $chanson")
    }

    @Test
    fun `étant donné une chanson vide lorsqu'on tente de sauvegarder la chanson du jour, un message d'erreur est affiché`() {
        val chanson = ""

        présentateur.sauvegarderChansonDuJour(chanson)

        Mockito.verify(mockVue).afficherMessageErreur("La chanson du jour ne peut pas être vide.")
    }
}
