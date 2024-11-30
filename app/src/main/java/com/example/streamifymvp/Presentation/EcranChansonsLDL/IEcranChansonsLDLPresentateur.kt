package com.example.streamifymvp.Presentation.EcranChansonsLDL

import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture

interface IEcranChansonsLDLPresentateur {

    fun obtenirChansonsDeLaListeDeLecture(playlistId: Int): List<Chanson>

    fun obtenirDetailsListeDeLecture(playlistId: Int): ListeDeLecture?

    fun obtenirPseudoArtiste(artisteId: Int): String
}