package com.example.streamifymvp.Presentation

import com.example.streamifymvp.Domaine.entitees.Artiste
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture

interface IModele {
    suspend fun obtenirToutesLesChansons(): List<Chanson>
    suspend fun obtenirNouveautés(): List<Chanson>
    suspend fun rechercherChansons(recherche: String): List<Chanson>
    suspend fun obtenirNouveauxArtistes(): List<Artiste>
    suspend fun ajouterChansonAuxFavoris(chanson: Chanson)
    suspend fun obtenirFavoris(): ListeDeLecture?
    suspend fun obtenirTousLesArtistes(): List<Artiste>
    suspend fun obtenirListeDeLectureParId(playlistId: Int): ListeDeLecture?
}
