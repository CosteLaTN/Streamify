package com.example.streamifymvp.Presentation.Lecture

import android.util.Log
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ChansonAvecArtiste
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import com.example.streamifymvp.Presentation.Modele
import kotlinx.coroutines.*

class LecturePresentateur(
    private val modele: Modele,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ILecturePresentateur {

    override fun ajouterAuxFavoris(chanson: Chanson) {
        CoroutineScope(ioDispatcher).launch {
            modele.ajouterChansonAuxFavoris(chanson)
        }
    }

    override fun obtenirFavoris(onResult: (ListeDeLecture?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val favoris = withContext(ioDispatcher) { modele.obtenirFavoris() }
            onResult(favoris)
        }
    }

    suspend fun obtenirChansonParId(chansonId: Int): Chanson {
        return withContext(ioDispatcher) {
            modele.obtenirToutesLesChansons().find { it.id == chansonId }
                ?: throw IllegalArgumentException("Chanson introuvable")
        }
    }

    suspend fun obtenirChansonsParGenre(genre: String): List<Chanson> {
        return withContext(ioDispatcher) {
            modele.obtenirToutesLesChansons().filter { it.genre == genre }
        }
    }

    suspend fun obtenirPseudoArtiste(artisteId: Int): String {
        return withContext(ioDispatcher) {
            val artiste = modele.obtenirTousLesArtistes().find { it.id == artisteId }
            artiste?.pseudoArtiste ?: "Artiste inconnu"
        }
    }

    suspend fun obtenirToutesLesListesDeLecture(): List<ListeDeLecture> {
        return withContext(ioDispatcher) {
            modele.obtenirToutesLesListesDeLecture()
        }
    }

    suspend fun ajouterChansonAPlaylist(playlistId: Int, chanson: Chanson) {
        withContext(ioDispatcher) {
            modele.ajouterChansonAPlaylist(playlistId, chanson)
        }
    }
    suspend fun obtenirFavorisSuspendu(): ListeDeLecture? {
        return try {
            modele.obtenirFavoris()
        } catch (e: Exception) {
            Log.e("LecturePresentateur", "Erreur lors de l'obtention des favoris : ${e.message}")
            null
        }
    }
    suspend fun obtenirChansonsDeLaListeDeLecture(playlistId: Int): List<Chanson> {
        return modele.obtenirChansonsDeLaListeDeLecture(playlistId)
    }



}
