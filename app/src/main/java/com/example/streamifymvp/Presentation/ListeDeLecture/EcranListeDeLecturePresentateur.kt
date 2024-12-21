package com.example.streamifymvp.Presentation.ListeDeLecture

import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import com.example.streamifymvp.Presentation.Modele
import kotlinx.coroutines.*

class EcranListeDeLecturePresentateur(
    private val vue: IEcranListeDeLecture,
    private val modele: Modele,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IEcranListeDeLecturePresentateur {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun chargerListesDeLecture() {
        scope.launch {
            try {
                val playlists = withContext(ioDispatcher) {
                    modele.obtenirToutesLesListesDeLecture()
                }
                if (playlists.isNotEmpty()) {
                    vue.rafraichirListeDeLecture(playlists)
                } else {
                    vue.afficherMessageErreur("Aucune playlist disponible.")
                }
            } catch (e: Exception) {
                vue.afficherMessageErreur("Erreur lors du chargement des playlists.")
            }
        }
    }

    override fun ajouterNouvellePlaylist(nom: String) {
        scope.launch {
            try {
                val nouvellePlaylist = ListeDeLecture(
                    id = System.currentTimeMillis().toInt(),
                    nom = nom,
                    chansons = mutableListOf()
                )
                withContext(ioDispatcher) {
                    modele.ajouterPlaylist(nouvellePlaylist)
                }
                chargerListesDeLecture()
                vue.afficherMessageSucces("Playlist '$nom' ajoutée avec succès.")
            } catch (e: Exception) {
                vue.afficherMessageErreur("Erreur lors de la création de la playlist.")
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
    }
}
