package com.example.streamifymvp.SourceDeDonnees

import com.example.streamifymvp.Domaine.Modeles.Artiste

interface ArtisteSourceDeDonnees {
    fun obtenirTousLesArtistes(): List<Artiste>
    fun obtenirArtisteParId(id: Int): Artiste?
    fun rechercherArtistes(recherche: String): List<Artiste>
}
