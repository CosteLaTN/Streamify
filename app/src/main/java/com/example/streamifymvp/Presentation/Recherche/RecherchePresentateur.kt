package com.example.streamifymvp.Presentation.Recherche

import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Presentation.IModele
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecherchePresentateur(
    private val modele: IModele,
    private val vue: RechercheContrat.IRechercheVue
) : RechercheContrat.IRecherchePresentateur {

    override fun effectuerRecherche(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val resultats = modele.rechercherChansons(query)
            withContext(Dispatchers.Main) {
                if (resultats.isEmpty()) {
                    vue.afficherMessageAucunResultat()
                } else {
                    vue.afficherResultats(resultats)
                }
            }
        }
    }
}
