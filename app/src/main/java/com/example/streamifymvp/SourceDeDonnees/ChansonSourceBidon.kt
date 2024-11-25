package com.example.streamifymvp.SourceDeDonnees

import android.util.Log
import com.example.streamifymvp.Domaine.Modeles.Artiste
import com.example.streamifymvp.Domaine.Modeles.Chanson
import com.example.streamifymvp.Domaine.Modeles.ListeDeLecture
import com.example.streamifymvp.R

class ChansonSourceBidon private constructor() : ChansonSourceDeDonnes {

    companion object {
        val instance: ChansonSourceBidon by lazy { ChansonSourceBidon() }
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
            imageArtiste = R.drawable.humps
        ),
        Artiste(
            id = 3,
            prenom = "Guy-Manuel de Homem-Christo",
            nom = "& Thomas Bangalter",
            pseudoArtiste = "Daft Punk",
            imageArtiste = R.drawable.random_access_memories
        )
    )

    private val chansons = listOf(
        Chanson(
            id = 1,
            nom = "Dont Cry",
            datePublication = "2019-09-17",
            genre = "Rock",
            dureeAlbum = "45:00",
            dureeMusique = "4:44",
            imageChanson = R.drawable.cry,
            fichierAudio = R.raw.dontcry,
            albumId = 1,
            artisteId = 1
        ),
        Chanson(
            id = 2,
            nom = "My Humps",
            datePublication = "2019-07-19",
            genre = "Rock",
            dureeAlbum = "60:00",
            dureeMusique = "3:45",
            imageChanson = R.drawable.humps,
            fichierAudio = R.raw.myhumps,
            albumId = 2,
            artisteId = 2
        ),
        Chanson(
            id = 3,
            nom = "I Gotta Feeling",
            datePublication = "2019-06-16",
            genre = "Rock",
            dureeAlbum = "50:00",
            dureeMusique = "4:49",
            imageChanson = R.drawable.igottafeeling,
            fichierAudio = R.raw.feeling,
            albumId = 2,
            artisteId = 2
        ),
        Chanson(
            id = 4,
            nom = "Aerodynamic",
            datePublication = "2020-01-01",
            genre = "Électronique",
            dureeAlbum = "60:00",
            dureeMusique = "3:30",
            imageChanson = R.drawable.discovery,
            fichierAudio = R.raw.aerodynamic,
            albumId = 3,
            artisteId = 3
        ),
        Chanson(
            id = 5,
            nom = "Harder Better Faster Stronger",
            datePublication = "2020-01-01",
            genre = "Électronique",
            dureeAlbum = "60:00",
            dureeMusique = "3:45",
            imageChanson = R.drawable.discovery,
            fichierAudio = R.raw.harder,
            albumId = 3,
            artisteId = 3
        ),
        Chanson(
            id = 6,
            nom = "One More Time",
            datePublication = "2020-01-01",
            genre = "Électronique",
            dureeAlbum = "60:00",
            dureeMusique = "5:20",
            imageChanson = R.drawable.discovery,
            fichierAudio = R.raw.one_more_time,
            albumId = 3,
            artisteId = 3
        ),
        Chanson(
            id = 7,
            nom = "Voyager",
            datePublication = "2020-01-01",
            genre = "Électronique",
            dureeAlbum = "60:00",
            dureeMusique = "3:50",
            imageChanson = R.drawable.discovery,
            fichierAudio = R.raw.voyager,
            albumId = 3,
            artisteId = 3
        ),
        Chanson(
            id = 8,
            nom = "Too Long",
            datePublication = "2020-01-01",
            genre = "Électronique",
            dureeAlbum = "60:00",
            dureeMusique = "10:00",
            imageChanson = R.drawable.discovery,
            fichierAudio = R.raw.too_long,
            albumId = 3,
            artisteId = 3
        ),
        Chanson(
            id = 9,
            nom = "Something About Us",
            datePublication = "2020-01-01",
            genre = "Électronique",
            dureeAlbum = "60:00",
            dureeMusique = "3:51",
            imageChanson = R.drawable.discovery,
            fichierAudio = R.raw.something_about_us,
            albumId = 3,
            artisteId = 3
        ),
        Chanson(
            id = 10,
            nom = "Give Life Back To Music",
            datePublication = "2020-01-01",
            genre = "Électronique",
            dureeAlbum = "60:00",
            dureeMusique = "4:35",
            imageChanson = R.drawable.random_access_memories,
            fichierAudio = R.raw.give_life_back_to_music,
            albumId = 4,
            artisteId = 3
        ),
        Chanson(
            id = 11,
            nom = "Instant Crush",
            datePublication = "2020-01-01",
            genre = "Électronique",
            dureeAlbum = "60:00",
            dureeMusique = "5:37",
            imageChanson = R.drawable.random_access_memories,
            fichierAudio = R.raw.instant_crush,
            albumId = 4,
            artisteId = 3
        ),
        Chanson(
            id = 12,
            nom = "Get Lucky",
            datePublication = "2020-01-01",
            genre = "Électronique",
            dureeAlbum = "60:00",
            dureeMusique = "6:07",
            imageChanson = R.drawable.random_access_memories,
            fichierAudio = R.raw.get_lucky,
            albumId = 4,
            artisteId = 3
        ),
        Chanson(
            id = 13,
            nom = "Fragments Of Time",
            datePublication = "2020-01-01",
            genre = "Électronique",
            dureeAlbum = "60:00",
            dureeMusique = "4:39",
            imageChanson = R.drawable.random_access_memories,
            fichierAudio = R.raw.fragments_of_time,
            albumId = 4,
            artisteId = 3
        ),
        Chanson(
            id = 14,
            nom = "Beyond",
            datePublication = "2020-01-01",
            genre = "Électronique",
            dureeAlbum = "60:00",
            dureeMusique = "4:50",
            imageChanson = R.drawable.random_access_memories,
            fichierAudio = R.raw.beyond,
            albumId = 4,
            artisteId = 3
        )
    )


    private val listesDeLecture = mutableListOf<ListeDeLecture>(
        ListeDeLecture(
            id = 1,
            nom = "Favoris",
            chansons = mutableListOf()
        )
    )

    override fun obtenirToutesLesChansons(): List<Chanson> {
        return chansons
    }

    override fun obtenirPlaylist(nom: String): ListeDeLecture? {
        return listesDeLecture.find { it.nom.equals(nom, ignoreCase = true) }
    }

    override fun ajouterChansonALaPlaylist(nomPlaylist: String, chanson: Chanson) {
        val playlist = obtenirPlaylist(nomPlaylist)
        if (playlist != null && !playlist.chansons.contains(chanson)) {
            playlist.chansons.add(chanson)
        }
    }

    override fun obtenirFavoris(): ListeDeLecture? {
        return obtenirPlaylist("Favoris")
    }

    override fun obtenirToutesLesListesDeLecture(): List<ListeDeLecture> {
        return listesDeLecture
    }

    override fun obtenirListeDeLectureParId(id: Int): ListeDeLecture? {
        return listesDeLecture.find { it.id == id }
    }

    override fun ajouterAuxFavoris(chanson: Chanson) {
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

    fun ajouterPlaylist(playlist: ListeDeLecture) {
        listesDeLecture.add(playlist)
        Log.d("ChansonSourceBidon", "Nouvelle playlist ajoutée : ${playlist.nom}")
    }

    fun ajouterChansonALaPlaylist(playlistId: Int, chanson: Chanson) {
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
