package com.example.streamifymvp.Presentation.profil

import com.example.streamifymvp.SourceDeDonnees.ISourceDeDonnee
import kotlinx.coroutines.*
import java.util.Locale

class ProfilPrésentateur(
    private val vue: ContratVuePrésentateurProfil.IProfilVue,
    private val service: ISourceDeDonnee,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ContratVuePrésentateurProfil.IProfilPrésentateur {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun chargerProfil() {
        scope.launch {
            try {
                val profil = withContext(ioDispatcher) { service.obtenirProfil() }
                if (profil != null) {
                    vue.afficherNomUtilisateur(profil.nom)
                    vue.afficherUsername(profil.username)
                } else {
                    vue.afficherMessageErreur("Erreur lors du chargement du profil.")
                }
            } catch (e: Exception) {
                vue.afficherMessageErreur("Erreur lors du chargement du profil.")
            }
        }
    }

    override fun modifierNomUtilisateur(nouveauNom: String) {
        if (nouveauNom.isBlank()) {
            vue.afficherMessageErreur("Le nom ne peut pas être vide.")
            return
        }

        scope.launch {
            try {
                val succes = withContext(ioDispatcher) { service.modifierNomUtilisateur(nouveauNom) }
                if (succes) {
                    vue.afficherNomUtilisateur(nouveauNom)
                    vue.afficherMessageSucces("Nom modifié avec succès.")
                } else {
                    vue.afficherMessageErreur("Erreur lors de la modification du nom.")
                }
            } catch (e: Exception) {
                vue.afficherMessageErreur("Erreur lors de la modification du nom.")
            }
        }
    }

    override fun modifierUsername(nouveauUsername: String) {
        if (nouveauUsername.isBlank()) {
            vue.afficherMessageErreur("Le nom d'utilisateur ne peut pas être vide.")
            return
        }

        scope.launch {
            try {
                val succes = withContext(ioDispatcher) { service.modifierUsername(nouveauUsername) }
                if (succes) {
                    vue.afficherUsername(nouveauUsername)
                    vue.afficherMessageSucces("Nom d'utilisateur modifié avec succès.")
                } else {
                    vue.afficherMessageErreur("Erreur lors de la modification du nom d'utilisateur.")
                }
            } catch (e: Exception) {
                vue.afficherMessageErreur("Erreur lors de la modification du nom d'utilisateur.")
            }
        }
    }

    override fun sauvegarderChansonDuJour(chanson: String) {
        if (chanson.isBlank()) {
            vue.afficherMessageErreur("La chanson du jour ne peut pas être vide.")
        } else {
            vue.afficherMessageSucces("Chanson du jour sauvegardée : $chanson")
        }
    }

    override fun gererChangementLangue() {
        val currentLanguage = Locale.getDefault().language
        val newLanguage = if (currentLanguage == "fr") "en" else "fr"
        vue.changerLangue(newLanguage)
    }

    override fun gererModificationNomUtilisateur(nouveauNom: String) {
        modifierNomUtilisateur(nouveauNom)
    }

    override fun gererModificationUsername(nouveauUsername: String) {
        modifierUsername(nouveauUsername)
    }

    fun onDestroy() {
        scope.cancel()
    }
}
