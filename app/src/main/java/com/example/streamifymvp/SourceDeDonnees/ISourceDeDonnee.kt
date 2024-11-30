package com.example.streamifymvp.SourceDeDonnees

import Profil
import com.example.streamifymvp.Domaine.entitees.Artiste
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import com.example.streamifymvp.Domaine.entitees.ShowDate

interface ISourceDeDonnee {
    fun obtenirProfil(): Profil?
    fun modifierNomUtilisateur(nouveauNom: String): Boolean
    fun modifierUsername(nouveauUsername: String): Boolean
    fun obtenirHistoriqueRecherche(): List<String>
    fun obtenirToutesLesDatesDeShow(): List<ShowDate>
    fun obtenirTousLesArtistes(): List<Artiste>
    fun obtenirArtisteParId(id: Int): Artiste?
    fun rechercherArtistes(recherche: String): List<Artiste>
    fun obtenirToutesLesChansons(): List<Chanson>

    fun obtenirPlaylist(nom: String): ListeDeLecture?

    fun ajouterChansonALaPlaylist(nomPlaylist: String, chanson: Chanson)

    fun obtenirFavoris(): ListeDeLecture?

    fun obtenirToutesLesListesDeLecture(): List<ListeDeLecture>

    fun obtenirListeDeLectureParId(id: Int): ListeDeLecture?

    fun ajouterAuxFavoris(chanson: Chanson)

    fun ajouterPlaylist(playlist: ListeDeLecture)

    fun ajouterChansonALaPlaylist(playlistId: Int, chanson: Chanson)
}
