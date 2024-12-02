package com.example.streamifymvp.Presentation.EcranChansonsLDL

import android.util.Log
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import com.example.streamifymvp.Presentation.Modele
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EcranChansonsLDLPresentateur(private val modèle: Modele) : IEcranChansonsLDLPresentateur {

    override suspend fun obtenirChansonsDeLaListeDeLecture(playlistId: Int): List<Chanson> {
        return withContext(Dispatchers.IO) {
            val playlist = modèle.obtenirListeDeLectureParId(playlistId)
            Log.d("EcranChansonsLDLPresentateur", "Playlist trouvée : ${playlist?.nom}, Chansons : ${playlist?.chansons?.map { it.nom }}")
            playlist?.chansons ?: emptyList()
        }
    }

    override suspend fun obtenirDetailsListeDeLecture(playlistId: Int): ListeDeLecture? {
        return withContext(Dispatchers.IO) {
            modèle.obtenirListeDeLectureParId(playlistId)
        }
    }

    override suspend fun obtenirPseudoArtiste(artisteId: Int): String {
        val artiste = modèle.obtenirTousLesArtistes().find { it.id == artisteId }
        return artiste?.pseudoArtiste ?: "Artiste inconnu"
    }
}
