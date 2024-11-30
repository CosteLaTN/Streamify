package com.example.streamifymvp.Presentation.Lecture

import com.example.streamifymvp.Domaine.entitees.Chanson

interface IEcranLecture {

     fun jouerChanson(chanson: Chanson)

     fun jouerSuivante()

     fun setupListeners()

     fun updateFavoriteButtonIcon()

     fun afficherListeDeLecturePourAjout()


}