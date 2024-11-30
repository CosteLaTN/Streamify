package com.example.streamifymvp.Presentation

import com.example.streamifymvp.Domaine.entitees.Artiste
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import com.example.streamifymvp.Domaine.Service.ArtisteService
import com.example.streamifymvp.Domaine.Service.ListeDeLectureService
import com.example.streamifymvp.Domaine.Service.ChansonService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Modele(
    private val chansonService: ChansonService,
    private val artisteService: ArtisteService,
    private val listeDeLectureService: ListeDeLectureService
) : IModele {

    private var currentPlaylistId: Int? = null

    override fun obtenirToutesLesChansons(): List<Chanson> {
        return chansonService.obtenirToutesLesChansons()
    }

    override fun obtenirNouveautés(): List<Chanson> {
        return chansonService.obtenirNouveautés()
    }

    override fun rechercherChansons(recherche: String): List<Chanson> {
        return chansonService.rechercherChansons(recherche)
    }

    override fun obtenirNouveauxArtistes(): List<Artiste> {
        return artisteService.obtenirTousLesArtistes()
    }

    override fun ajouterChansonAuxFavoris(chanson: Chanson) {
        chansonService.ajouterAuxFavoris(chanson)
    }

     override suspend fun obtenirFavoris(): ListeDeLecture? {
        return withContext(Dispatchers.IO) {
            listeDeLectureService.obtenirListeDeLectureParNom("Favoris")
        }
    }

    override fun obtenirTousLesArtistes(): List<Artiste> {
        return artisteService.obtenirTousLesArtistes()
    }

    override fun obtenirListeDeLectureParId(playlistId: Int): ListeDeLecture? {
        return listeDeLectureService.obtenirListeDeLectureParId(playlistId)
    }
}
