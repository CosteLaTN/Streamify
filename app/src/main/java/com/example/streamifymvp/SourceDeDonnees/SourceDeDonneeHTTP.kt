package com.example.streamifymvp.SourceDeDonnees

import com.example.streamifymvp.Domaine.entitees.*

class SourceDeDonneeHTTP : ISourceDeDonnee {

    private val api = RetrofitClient.instance.create(ApiService::class.java)

    override fun obtenirProfil(): Profil? = null

    override fun modifierNomUtilisateur(nouveauNom: String): Boolean = false

    override fun modifierUsername(nouveauUsername: String): Boolean = false

    override fun obtenirHistoriqueRecherche(): List<String> = emptyList()

    override fun obtenirToutesLesDatesDeShow(): List<ShowDate> = emptyList()

    override suspend fun obtenirTousLesArtistes(): List<Artiste> {
        return api.getAllArtistes()
    }

    override suspend fun obtenirArtisteParId(id: Int): Artiste? {
        return api.getArtisteById(id)
    }

    override suspend fun rechercherArtistes(recherche: String): List<Artiste> {
        return api.searchArtistes(recherche)
    }

    override suspend fun obtenirToutesLesChansons(): List<Chanson> {
        return api.getAllChansons()
    }

    override suspend fun obtenirToutesLesListesDeLecture(): List<ListeDeLecture> {
        return api.getAllPlaylists()
    }

    override suspend fun obtenirListeDeLectureParId(id: Int): ListeDeLecture? {
        return api.getPlaylistById(id)
    }

    override suspend fun ajouterPlaylist(playlist: ListeDeLecture) {
        api.createPlaylist(playlist)
    }

    override suspend fun ajouterChansonALaPlaylist(playlistId: Int, chanson: Chanson) {
        api.addSongToPlaylist(playlistId, chanson.id)
    }

    override suspend fun obtenirPlaylist(nom: String): ListeDeLecture? {
       val playlists = runCatching { obtenirToutesLesListesDeLecture() }.getOrDefault(emptyList())
       return playlists.find { it.nom.equals(nom, ignoreCase = true) }
    }

   override suspend fun ajouterChansonALaPlaylist(nomPlaylist: String, chanson: Chanson) {
        val playlist = obtenirPlaylist(nomPlaylist)
          ?: throw Exception("Playlist introuvable : $nomPlaylist")
       runCatching { ajouterChansonALaPlaylist(playlist.id, chanson) }
    }

   override suspend fun obtenirFavoris(): ListeDeLecture? = obtenirPlaylist("Favoris")

    override suspend fun ajouterAuxFavoris(chanson: Chanson) {
        val favoris = obtenirFavoris()
            ?: throw Exception("La playlist des favoris est introuvable.")
        ajouterChansonALaPlaylist(favoris.id, chanson)
    }
}
