package com.example.streamify.Presentation.Accueil

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.streamifymvp.Presentation.Accueil.IAccueilPresentateur
import com.example.streamifymvp.Presentation.Modele

class AccueilPresentateur(
    private val vue: AccueilVue,
    private val modèle: Modele
) : IAccueilPresentateur {

    override fun chargerAccueil() {

        CoroutineScope(Dispatchers.Main).launch {
            try {

                val chansons = modèle.obtenirToutesLesChansons()
                val nouveautés = modèle.obtenirNouveautés()
                val artistes = modèle.obtenirNouveauxArtistes()


                vue.afficherChansons(chansons)
                vue.afficherNouveautés(nouveautés)
                vue.afficherNouveauxArtistes(artistes)
            } catch (e: Exception) {

                vue.afficherMessageAucunRésultat()
            }
        }
    }

    override fun rechercherChansons(recherche: String) {

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val résultats = modèle.rechercherChansons(recherche)
                if (résultats.isEmpty()) {
                    vue.afficherMessageAucunRésultat()
                } else {
                    vue.afficherChansons(résultats)
                }
            } catch (e: Exception) {
                vue.afficherMessageAucunRésultat()
            }
        }
    }

}
