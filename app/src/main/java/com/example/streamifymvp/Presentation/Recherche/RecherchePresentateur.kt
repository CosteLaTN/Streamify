package com.example.streamifymvp.Presentation.Recherche

import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Presentation.IModele
import kotlinx.coroutines.*

class RecherchePresentateur(
    private val modele: IModele,
    private val vue: RechercheContrat.IRechercheVue
) : RechercheContrat.IRecherchePresentateur {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun effectuerRecherche(query: String) {
        scope.launch {
            try {
                val resultats = withContext(Dispatchers.IO) { modele.rechercherChansons(query) }
                if (resultats.isEmpty()) {
                    vue.afficherMessageAucunResultat()
                } else {
                    vue.afficherResultats(resultats)
                }
            } catch (e: Exception) {
                vue.afficherMessageAucunResultat()
            }
        }
    }

    fun onDestroy() {
        scope.cancel()
    }
}
