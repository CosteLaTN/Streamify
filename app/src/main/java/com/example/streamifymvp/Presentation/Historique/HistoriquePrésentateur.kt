package com.example.streamifymvp.Presentation.Historique
import com.example.streamifymvp.SourceDeDonnees.ISourceDeDonnee
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoriquePrésentateur(
    private val vue: ContratVuePrésentateurHistorique.IHistoriqueVue,
    private val service: ISourceDeDonnee,
    private val ioDispatcher: CoroutineDispatcher
) : ContratVuePrésentateurHistorique.IHistoriquePrésentateur {

    override fun chargerHistoriqueRecherche() {
        CoroutineScope(ioDispatcher).launch {
            try {
                val historique = service.obtenirHistoriqueRecherche()

                withContext(Dispatchers.Main) {
                    if (historique.isNotEmpty()) {
                        vue.afficherHistoriqueRecherche(historique)
                    } else {
                        vue.afficherMessageErreur("Aucun historique trouvé.")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    vue.afficherMessageErreur("Erreur lors du chargement de l'historique de recherche")
                }
            }
        }
    }
}
