package com.example.streamifymvp.Presentation.EcranChansonsLDL

import android.util.Log
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import com.example.streamifymvp.Presentation.Modele

class EcranChansonsLDLPresentateur(private val modèle: Modele) :IEcranChansonsLDLPresentateur {

    override fun obtenirChansonsDeLaListeDeLecture(playlistId: Int): List<Chanson> {
        val playlist = modèle.obtenirListeDeLectureParId(playlistId)
        Log.d("EcranChansonsLDLPresentateur", "Playlist trouvée : ${playlist?.nom}, Chansons : ${playlist?.chansons?.map { it.nom }}")
        return playlist?.chansons ?: emptyList()
    }

    override fun obtenirDetailsListeDeLecture(playlistId: Int): ListeDeLecture? {
        return modèle.obtenirListeDeLectureParId(playlistId)
    }

    override fun obtenirPseudoArtiste(artisteId: Int): String {
        val artiste = modèle.obtenirTousLesArtistes().find { it.id == artisteId }
        return artiste?.pseudoArtiste ?: "Artiste inconnu"
    }
}
