package com.example.streamifymvp.SourceDeDonnees

import android.util.Log
import com.example.streamifymvp.Domaine.entitees.*
import java.text.SimpleDateFormat
import java.util.Locale

class SourceDeDonneeHTTP : ISourceDeDonnee {

    private var profil: Profil? = Profil("Jean Dupont", "jdupont")
    companion object {
        val instance: SourceDeDonneeHTTP by lazy { SourceDeDonneeHTTP() }
    }
    override fun obtenirProfil(): Profil? {
        return profil
    }
    private val api = RetrofitClient.instance.create(ApiService::class.java)



    override fun modifierNomUtilisateur(nouveauNom: String): Boolean {
        return if (profil != null) {
            profil!!.nom = nouveauNom
            true
        } else {
            false
        }
    }

    override fun modifierUsername(nouveauUsername: String): Boolean {
        return if (profil != null) {
            profil!!.username = nouveauUsername
            true
        } else {
            false
        }
    }

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
        val chansons = api.getAllChansons()
        chansons.forEach { chanson ->
            Log.d("SourceDeDonneeHTTP", "Chanson: ${chanson.nom}, artisteId: ${chanson.artiste}")
        }
        return chansons
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

    override suspend fun obtenirFavoris(): ListeDeLecture? {
        return obtenirPlaylist("Favoris")
    }

    override suspend fun ajouterAuxFavoris(chanson: Chanson) {
        val favoris = obtenirFavoris()
            ?: throw Exception("La playlist des favoris est introuvable.")
        ajouterChansonALaPlaylist(favoris.id, chanson)
    }


    private val historique = listOf("Daft Punk", "Discovery", "League of legends")

    override fun obtenirHistoriqueRecherche(): List<String> {
        return historique
    }


    private val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val datesDisponibles = listOf(
        ShowDate(
            title = "Concert de Daft Punk",
            details = "Un concert légendaire de Daft Punk",
            date = format.parse("01/01/2024")!!,
            location = "Paris, France"
        ),
        ShowDate(
            title = "Concert de Adele",
            details = "Performance live de Adele",
            date = format.parse("05/01/2024")!!,
            location = "Londres, UK"
        ),
        ShowDate(
            title = "Festival de musique électronique",
            details = "Festival avec de nombreux artistes de musique électronique",
            date = format.parse("10/01/2024")!!,
            location = "Berlin, Allemagne"
        )
    )

    override fun obtenirToutesLesDatesDeShow(): List<ShowDate> {
        return datesDisponibles
    }




}
