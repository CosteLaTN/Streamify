package com.example.streamifymvp.Domaine.Service

import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import com.example.streamifymvp.SourceDeDonnees.ISourceDeDonnee

class ListeDeLectureService(private val chansonSource: ISourceDeDonnee) {

    suspend fun obtenirToutesLesListesDeLecture(): List<ListeDeLecture> {
        return chansonSource.obtenirToutesLesListesDeLecture()
    }

    suspend fun obtenirListeDeLectureParId(playlistId: Int): ListeDeLecture? {
        return chansonSource.obtenirListeDeLectureParId(playlistId)
    }

    suspend fun obtenirListeDeLectureParNom(nom: String): ListeDeLecture? {
        val listesDeLecture = chansonSource.obtenirToutesLesListesDeLecture()
        return listesDeLecture.find { it.nom == nom }
    }

    suspend fun obtenirChansonsDeLaListeDeLecture(playlistId: Int): List<Chanson> {
        val playlist = chansonSource.obtenirListeDeLectureParId(playlistId)
        return playlist?.chansons ?: emptyList()
    }

   suspend fun ajouterChansonAPlaylist(playlistId: Int, chanson: Chanson) {
       val playlist = chansonSource.obtenirListeDeLectureParId(playlistId)
       playlist?.let {
            if (!it.chansons.contains(chanson)) {
                it.chansons.add(chanson)
            }
       }
   }
    suspend fun ajouterPlaylist(playlist: ListeDeLecture) {
        chansonSource.ajouterPlaylist(playlist)
    }






}
