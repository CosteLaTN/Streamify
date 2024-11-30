package com.example.streamifymvp.Presentation

import com.example.streamifymvp.Domaine.entitees.Artiste
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture

interface IModele {

    fun obtenirToutesLesChansons(): List<Chanson>
    fun obtenirNouveautés(): List<Chanson>
    fun rechercherChansons(recherche: String): List<Chanson>
    fun obtenirNouveauxArtistes(): List<Artiste>
    fun ajouterChansonAuxFavoris(chanson: Chanson)
    suspend fun obtenirFavoris(): ListeDeLecture?
    fun obtenirTousLesArtistes(): List<Artiste>
    fun obtenirListeDeLectureParId(playlistId: Int): ListeDeLecture?

}