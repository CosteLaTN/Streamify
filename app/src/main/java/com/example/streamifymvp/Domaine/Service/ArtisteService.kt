package com.example.streamifymvp.Domaine.Service

import com.example.streamifymvp.Domaine.Modeles.Artiste
import com.example.streamifymvp.SourceDeDonnees.ArtisteSourceDeDonnees

class ArtisteService(private val source: ArtisteSourceDeDonnees) {

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
