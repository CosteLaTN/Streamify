package com.example.streamifymvp.SourceDeDonnees

import com.example.streamifymvp.Domaine.entitees.*

interface ISourceDeDonnee {
    fun obtenirProfil(): Profil?
    fun modifierNomUtilisateur(nouveauNom: String): Boolean
    fun modifierUsername(nouveauUsername: String): Boolean
    fun obtenirHistoriqueRecherche(): List<String>
    fun obtenirToutesLesDatesDeShow(): List<ShowDate>


    suspend fun obtenirTousLesArtistes(): List<Artiste>
    suspend fun obtenirArtisteParId(id: Int): Artiste?
    suspend fun rechercherArtistes(recherche: String): List<Artiste>
    suspend fun obtenirToutesLesChansons(): List<Chanson>
    suspend fun obtenirToutesLesListesDeLecture(): List<ListeDeLecture>
    suspend fun obtenirListeDeLectureParId(id: Int): ListeDeLecture?
    suspend fun ajouterPlaylist(playlist: ListeDeLecture)
    suspend fun ajouterChansonALaPlaylist(playlistId: Int, chanson: Chanson)


     suspend fun obtenirPlaylist(nom: String): ListeDeLecture?

    suspend fun obtenirFavoris(): ListeDeLecture?
    suspend fun ajouterAuxFavoris(chanson: Chanson)
}
