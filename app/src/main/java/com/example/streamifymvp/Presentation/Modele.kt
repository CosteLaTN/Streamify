package com.example.streamifymvp.Presentation

import com.example.streamifymvp.Domaine.entitees.Artiste
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import com.example.streamifymvp.Domaine.Service.ArtisteService
import com.example.streamifymvp.Domaine.Service.ChansonService
import com.example.streamifymvp.Domaine.Service.ListeDeLectureService
import com.example.streamifymvp.SourceDeDonnees.SourceDeDonneeHTTP

class Modele : IModele {

    private val sourceDeDonneeHTTP = SourceDeDonneeHTTP()
    private val chansonService = ChansonService(sourceDeDonneeHTTP)
    private val artisteService = ArtisteService(sourceDeDonneeHTTP)
    private val listeDeLectureService = ListeDeLectureService(sourceDeDonneeHTTP)

    override suspend fun obtenirToutesLesChansons(): List<Chanson> {
        return chansonService.obtenirToutesLesChansons()
    }

    override suspend fun obtenirNouveautés(): List<Chanson> {
        return chansonService.obtenirNouveautés()
    }

    override suspend fun rechercherChansons(recherche: String): List<Chanson> {
        return chansonService.rechercherChansons(recherche)
    }

    override suspend fun obtenirNouveauxArtistes(): List<Artiste> {
        return artisteService.obtenirTousLesArtistes()
    }

    override suspend fun ajouterChansonAuxFavoris(chanson: Chanson) {
        chansonService.ajouterAuxFavoris(chanson)
    }

    override suspend fun obtenirFavoris(): ListeDeLecture? {
        return listeDeLectureService.obtenirListeDeLectureParNom("Favoris")
    }

    override suspend fun obtenirTousLesArtistes(): List<Artiste> {
        return artisteService.obtenirTousLesArtistes()
    }

    override suspend fun obtenirListeDeLectureParId(playlistId: Int): ListeDeLecture? {
        return listeDeLectureService.obtenirListeDeLectureParId(playlistId)
    }
    suspend fun obtenirToutesLesListesDeLecture(): List<ListeDeLecture>{
        return listeDeLectureService.obtenirToutesLesListesDeLecture()
    }
    suspend fun ajouterChansonAPlaylist(playlistId: Int, chanson: Chanson){
        return listeDeLectureService.ajouterChansonAPlaylist(playlistId, chanson)
    }
    suspend fun ajouterPlaylist(playlist: ListeDeLecture) {
        listeDeLectureService.ajouterPlaylist(playlist)
    }

}
