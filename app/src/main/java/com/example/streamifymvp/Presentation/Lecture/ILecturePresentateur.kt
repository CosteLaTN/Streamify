package com.example.streamifymvp.Presentation.Lecture

import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture

interface ILecturePresentateur {

    fun ajouterAuxFavoris(chanson: Chanson)

    fun obtenirFavoris(onResult: (ListeDeLecture?) -> Unit)
}