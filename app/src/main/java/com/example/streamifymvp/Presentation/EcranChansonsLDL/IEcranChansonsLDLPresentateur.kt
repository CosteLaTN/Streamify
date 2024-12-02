package com.example.streamifymvp.Presentation.EcranChansonsLDL

import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture

interface IEcranChansonsLDLPresentateur {

    suspend fun obtenirChansonsDeLaListeDeLecture(playlistId: Int): List<Chanson>

    suspend fun obtenirDetailsListeDeLecture(playlistId: Int): ListeDeLecture?

    suspend fun obtenirPseudoArtiste(artisteId: Int): String
}
