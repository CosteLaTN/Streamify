package com.example.streamify.Presentation.Accueil

import com.example.streamifymvp.Presentation.Modele

class AccueilPresentateur(
    private val vue: AccueilVue,
    private val modèle: Modele
) {

    fun chargerAccueil() {
        val chansons = modèle.obtenirToutesLesChansons()
        val nouveautés = modèle.obtenirNouveautés()
        val artistes = modèle.obtenirNouveauxArtistes()

        vue.afficherChansons(chansons)
        vue.afficherNouveautés(nouveautés)
        vue.afficherNouveauxArtistes(artistes)
    }

    fun rechercherChansons(recherche: String) {
        val résultats = modèle.rechercherChansons(recherche)
        if (résultats.isEmpty()) {
            vue.afficherMessageAucunRésultat()
        } else {
            vue.afficherChansons(résultats)
        }
    }
}

