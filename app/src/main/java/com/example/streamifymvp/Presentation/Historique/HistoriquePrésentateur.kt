import com.example.streamifymvp.SourceDeDonnees.SourceDeDonneeBidon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoriquePrésentateur(
    private val vue: ContratVuePrésentateurHistorique.IHistoriqueVue
) : ContratVuePrésentateurHistorique.IHistoriquePrésentateur {

    private val service = SourceDeDonneeBidon()

    override fun chargerHistoriqueRecherche() {
        CoroutineScope(Dispatchers.IO).launch {
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