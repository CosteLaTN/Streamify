package com.example.streamify.Presentation.Accueil

import com.example.streamifymvp.Domaine.Modeles.Artiste
import com.example.streamifymvp.Domaine.Modeles.Chanson


interface AccueilVue {
    fun afficherChansons(chansons: List<Chanson>)
    fun afficherNouveautés(nouveautés: List<Chanson>)
    fun afficherNouveauxArtistes(artistes: List<Artiste>)
    fun afficherMessageAucunRésultat()
}
