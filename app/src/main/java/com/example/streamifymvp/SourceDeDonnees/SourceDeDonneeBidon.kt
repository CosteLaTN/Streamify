package com.example.streamifymvp.SourceDeDonnees


import android.util.Log
import com.example.streamifymvp.Domaine.entitees.Artiste
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import java.text.SimpleDateFormat
import com.example.streamifymvp.Domaine.entitees.ShowDate
import com.example.streamifymvp.Domaine.entitees.Profil
import com.example.streamifymvp.R
import java.util.Locale

class SourceDeDonneeBidon : ISourceDeDonnee {

    private var profil: Profil? = Profil("Jean Dupont", "jdupont") // Exemple de données
    companion object {
        val instance: SourceDeDonneeBidon by lazy { SourceDeDonneeBidon() }
    }
    override fun obtenirProfil(): Profil? {
        return profil
    }

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


    private val artistes = listOf(
        Artiste(
            id = 1,
            prenom = "Axl",
            nom = "Rose",
            pseudoArtiste = "Guns N' Roses",
            imageArtiste = R.drawable.cry
        ),
        Artiste(
            id = 2,
            prenom = "will.i.am",
            nom = "",
            pseudoArtiste = "The Black Eyed Peas",
            imageArtiste = R.drawable.placeholder_image
        ),
        Artiste(
            id = 3,
            prenom = "Guy-Manuel de Homem-Christo",
            nom = "& Thomas Bangalter",
            pseudoArtiste = "Daft Punk",
            imageArtiste = R.drawable.random_access_memories
        )
    )


    override suspend fun obtenirTousLesArtistes(): List<Artiste> = artistes

    override suspend fun obtenirArtisteParId(id: Int): Artiste? {
        return artistes.find { it.id == id }
    }

    override suspend fun rechercherArtistes(recherche: String): List<Artiste> {
        val rechercheMinuscule = recherche.lowercase()
        return artistes.filter {
            it.pseudoArtiste.lowercase().contains(rechercheMinuscule)
        }
    }


    private val chansons = listOf(
        Chanson(
            id = 1,
            nom = "Dont Cry",
            datePublication = "2019-09-17",
            genre = "Rock",
            dureeAlbum = "45:00",
            dureeMusique = "4:44",
            imageChanson = "http://192.168.182.1/images/cry.jpg",
            fichierAudio = "http://192.168.182.1/audio/lovin.mp3",
            albumId = 1,
            artisteId = 1
        )
    )


    private val listesDeLecture = mutableListOf<ListeDeLecture>(
        ListeDeLecture(
            id = 1,
            nom = "Favoris",
            chansons = mutableListOf()
        )
    )

    override suspend fun obtenirToutesLesChansons(): List<Chanson> {
        return chansons
    }

    override suspend fun obtenirPlaylist(nom: String): ListeDeLecture? {
        return listesDeLecture.find { it.nom.equals(nom, ignoreCase = true) }
    }

    override suspend fun ajouterChansonALaPlaylist(nomPlaylist: String, chanson: Chanson) {
        val playlist = obtenirPlaylist(nomPlaylist)
        if (playlist != null && !playlist.chansons.contains(chanson)) {
            playlist.chansons.add(chanson)
        }
    }

    override suspend fun obtenirFavoris(): ListeDeLecture? {
        return obtenirPlaylist("Favoris")
    }

    override suspend fun obtenirToutesLesListesDeLecture(): List<ListeDeLecture> {
        return listesDeLecture
    }

    override suspend fun obtenirListeDeLectureParId(id: Int): ListeDeLecture? {
        return listesDeLecture.find { it.id == id }
    }

    override suspend fun ajouterAuxFavoris(chanson: Chanson) {
        val favoris = listesDeLecture.find { it.nom == "Favoris" }
        if (favoris != null) {
            if (!favoris.chansons.contains(chanson)) {
                favoris.chansons.add(chanson)
                Log.d("ChansonSourceBidon", "Chanson '${chanson.nom}' ajoutée à 'Favoris'.")
            } else {
                Log.d("ChansonSourceBidon", "Chanson '${chanson.nom}' déjà dans 'Favoris'.")
            }
        } else {
            Log.e("ChansonSourceBidon", "Erreur : Playlist 'Favoris' introuvable.")
        }
    }

    override suspend fun ajouterPlaylist(playlist: ListeDeLecture) {
        listesDeLecture.add(playlist)
        Log.d("ChansonSourceBidon", "Nouvelle playlist ajoutée : ${playlist.nom}")
    }

    override suspend fun ajouterChansonALaPlaylist(playlistId: Int, chanson: Chanson) {
        val playlist = listesDeLecture.find { it.id == playlistId }
        if (playlist != null) {
            if (!playlist.chansons.contains(chanson)) {
                playlist.chansons.add(chanson)
                Log.d("ChansonSourceBidon", "Chanson '${chanson.nom}' ajoutée à la playlist '${playlist.nom}'.")
            }
        } else {
            Log.e("ChansonSourceBidon", "Playlist introuvable pour l'ID $playlistId.")
        }
    }
}
