package com.example.streamifymvp.Presentation

import com.example.streamifymvp.Domaine.Modeles.Artiste
import com.example.streamifymvp.Domaine.Modeles.Chanson
import com.example.streamifymvp.Domaine.Modeles.ListeDeLecture
import com.example.streamifymvp.Domaine.Service.ArtisteService
import com.example.streamifymvp.Domaine.Service.ListeDeLectureService
import com.example.streamifymvp.Domaine.Service.ChansonService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Modele(
    private val chansonService: ChansonService,
    private val artisteService: ArtisteService,
    private val listeDeLectureService: ListeDeLectureService
) {

    private var currentPlaylistId: Int? = null

    fun obtenirToutesLesChansons(): List<Chanson> {
        return chansonService.obtenirToutesLesChansons()
    }

    fun obtenirNouveautés(): List<Chanson> {
        return chansonService.obtenirNouveautés()
    }

    fun rechercherChansons(recherche: String): List<Chanson> {
        return chansonService.rechercherChansons(recherche)
    }

    fun obtenirNouveauxArtistes(): List<Artiste> {
        return artisteService.obtenirTousLesArtistes()
    }

    fun ajouterChansonAuxFavoris(chanson: Chanson) {
        chansonService.ajouterAuxFavoris(chanson)
    }

    suspend fun obtenirFavoris(): ListeDeLecture? {
        return withContext(Dispatchers.IO) {
            listeDeLectureService.obtenirListeDeLectureParNom("Favoris")
        }
    }

    fun obtenirTousLesArtistes(): List<Artiste> {
        return artisteService.obtenirTousLesArtistes()
    }

    fun obtenirListeDeLectureParId(playlistId: Int): ListeDeLecture? {
        return listeDeLectureService.obtenirListeDeLectureParId(playlistId)
    }
}
