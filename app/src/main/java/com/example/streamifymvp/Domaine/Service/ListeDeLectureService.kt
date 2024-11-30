package com.example.streamifymvp.Domaine.Service

import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import com.example.streamifymvp.SourceDeDonnees.ISourceDeDonnee

class ListeDeLectureService(private val chansonSource: ISourceDeDonnee) {

    fun obtenirToutesLesListesDeLecture(): List<ListeDeLecture> {
        return chansonSource.obtenirToutesLesListesDeLecture()
    }

    fun obtenirListeDeLectureParId(playlistId: Int): ListeDeLecture? {
        return chansonSource.obtenirListeDeLectureParId(playlistId)
    }

    fun obtenirListeDeLectureParNom(nom: String): ListeDeLecture? {
        val listesDeLecture = chansonSource.obtenirToutesLesListesDeLecture()
        return listesDeLecture.find { it.nom == nom }
    }

    fun obtenirChansonsDeLaListeDeLecture(playlistId: Int): List<Chanson> {
        val playlist = chansonSource.obtenirListeDeLectureParId(playlistId)
        return playlist?.chansons ?: emptyList()
    }

   // fun ajouterChansonAPlaylist(playlistId: Int, chanson: Chanson) {
   //     val playlist = chansonSource.obtenirListeDeLectureParId(playlistId)
   //     playlist?.let {
   //         if (!it.chansons.contains(chanson)) {
    //            it.chansons.add(chanson)
    //        }
    //    }
   // }






}
