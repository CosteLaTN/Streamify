package com.example.streamifymvp.SourceDeDonnees

import com.example.streamifymvp.Domaine.Modeles.Chanson
import com.example.streamifymvp.Domaine.Modeles.ListeDeLecture

interface ChansonSourceDeDonnes {
    fun obtenirToutesLesChansons(): List<Chanson>

    fun obtenirPlaylist(nom: String): ListeDeLecture?

    fun ajouterChansonALaPlaylist(nomPlaylist: String, chanson: Chanson)

    fun obtenirFavoris(): ListeDeLecture?

    fun obtenirToutesLesListesDeLecture(): List<ListeDeLecture>

    fun obtenirListeDeLectureParId(id: Int): ListeDeLecture?

    fun ajouterAuxFavoris(chanson: Chanson)
}
