package com.example.streamify.Presentation.Accueil

import com.example.streamifymvp.Domaine.entitees.Artiste
import com.example.streamifymvp.Domaine.entitees.Chanson


interface AccueilVue {
    fun afficherChansons(chansons: List<Chanson>)
    fun afficherNouveautés(nouveautés: List<Chanson>)
    fun afficherNouveauxArtistes(artistes: List<Artiste>)
    fun afficherMessageAucunRésultat()
}
