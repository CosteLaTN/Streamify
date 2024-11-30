package com.example.streamifymvp.Domaine.Service

import com.example.streamifymvp.Domaine.entitees.Artiste
import com.example.streamifymvp.SourceDeDonnees.ISourceDeDonnee

class ArtisteService(private val source: ISourceDeDonnee) {

    fun obtenirTousLesArtistes(): List<Artiste> {
        return source.obtenirTousLesArtistes()
    }

    fun obtenirArtisteParId(id: Int): Artiste? {
        return source.obtenirArtisteParId(id)
    }

    fun rechercherArtistes(recherche: String): List<Artiste> {
        return source.rechercherArtistes(recherche)
    }
}
