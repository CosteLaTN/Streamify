package com.example.streamifymvp.Presentation.EcranChansonsLDL

import android.util.Log
import com.example.streamifymvp.Domaine.Modeles.Chanson
import com.example.streamifymvp.Domaine.Modeles.ListeDeLecture
import com.example.streamifymvp.Presentation.Modele

class EcranChansonsLDLPresentateur(private val modèle: Modele) {

    fun obtenirChansonsDeLaListeDeLecture(playlistId: Int): List<Chanson> {
        val playlist = modèle.obtenirListeDeLectureParId(playlistId)
        Log.d("EcranChansonsLDLPresentateur", "Playlist trouvée : ${playlist?.nom}, Chansons : ${playlist?.chansons?.map { it.nom }}")
        return playlist?.chansons ?: emptyList()
    }

    fun obtenirDetailsListeDeLecture(playlistId: Int): ListeDeLecture? {
        return modèle.obtenirListeDeLectureParId(playlistId)
    }

    fun obtenirPseudoArtiste(artisteId: Int): String {
        val artiste = modèle.obtenirTousLesArtistes().find { it.id == artisteId }
        return artiste?.pseudoArtiste ?: "Artiste inconnu"
    }
}
