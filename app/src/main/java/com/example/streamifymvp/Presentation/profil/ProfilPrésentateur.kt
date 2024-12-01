package com.example.streamifymvp.Presentation.profil
import android.util.Log
import com.example.streamifymvp.SourceDeDonnees.SourceDeDonneeBidon
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.CoroutineContext

class ProfilPrésentateur(
    private val vue: ContratVuePrésentateurProfil.IProfilVue,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val service: SourceDeDonneeBidon
) : ContratVuePrésentateurProfil.IProfilPrésentateur, CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = dispatcher + job

    override fun chargerProfil() {
        launch {
            try {
                val profil = withContext(Dispatchers.IO) { service.obtenirProfil() }
                if (profil != null) {
                    vue.afficherNomUtilisateur(profil.nom)
                    vue.afficherUsername(profil.username)
                } else {
                    vue.afficherMessageErreur("Erreur lors du chargement du profil")
                }
            } catch (e: Exception) {
                vue.afficherMessageErreur("Erreur lors du chargement du profil")
            }
        }
    }

    override fun modifierNomUtilisateur(nouveauNom: String) {
        if (nouveauNom.isBlank()) {
            vue.afficherMessageErreur("Le nom ne peut pas être vide")
            return
        }
        val succes = service.modifierNomUtilisateur(nouveauNom)
        if (succes) {
            vue.afficherNomUtilisateur(nouveauNom)
        } else {
            vue.afficherMessageErreur("Erreur lors de la modification du nom")
        }
    }

    override fun modifierUsername(nouveauUsername: String) {
        if (nouveauUsername.isBlank()) {
            vue.afficherMessageErreur("Le nom d'utilisateur ne peut pas être vide")
            return
        }
        val succes = service.modifierUsername(nouveauUsername)
        if (succes) {
            vue.afficherUsername(nouveauUsername)
        } else {
            vue.afficherMessageErreur("Erreur lors de la modification du nom d'utilisateur")
        }
    }

    override fun sauvegarderChansonDuJour(chanson: String) {
        if (chanson.isBlank()) {
            vue.afficherMessageErreur("La chanson du jour ne peut pas être vide.")
        } else {
            Log.d("ProfilPrésentateur", "Chanson du jour : $chanson")
            vue.afficherMessageSucces("Chanson du jour sauvegardée : $chanson")
        }
    }

    override fun gererChangementLangue() {
        val currentLanguage = Locale.getDefault().language
        if (currentLanguage == "fr") {
            vue.changerLangue("en")
        } else {
            vue.changerLangue("fr")
        }
    }

    override fun gererModificationNomUtilisateur(nouveauNom: String) {
        if (nouveauNom.isBlank()) {
            vue.afficherMessageErreur("Le nom ne peut pas être vide")
        } else {
            val succes = service.modifierNomUtilisateur(nouveauNom)
            if (succes) {
                vue.afficherNomUtilisateur(nouveauNom)
                vue.afficherDialogueModificationUsername()
            } else {
                vue.afficherMessageErreur("Erreur lors de la modification du nom")
            }
        }
    }

    override fun gererModificationUsername(nouveauUsername: String) {
        if (nouveauUsername.isBlank()) {
            vue.afficherMessageErreur("Le nom d'utilisateur ne peut pas être vide")
        } else {
            val succes = service.modifierUsername(nouveauUsername)
            if (succes) {
                vue.afficherUsername(nouveauUsername)
            } else {
                vue.afficherMessageErreur("Erreur lors de la modification du nom d'utilisateur")
            }
        }
    }

    fun onDestroy() {
        job.cancel()
    }
}
