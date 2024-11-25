package com.example.streamifymvp.Presentation.Lecture

import com.example.streamifymvp.Domaine.Modeles.Chanson
import com.example.streamifymvp.Presentation.Modele
import com.example.streamifymvp.Domaine.Modeles.ListeDeLecture
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LecturePresentateur(private val modele: Modele) : CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun ajouterAuxFavoris(chanson: Chanson) {
        launch {
            withContext(Dispatchers.IO) {
                modele.ajouterChansonAuxFavoris(chanson)
            }
        }
    }

    fun obtenirFavoris(onResult: (ListeDeLecture?) -> Unit) {
        launch {
            val favoris = withContext(Dispatchers.IO) { modele.obtenirFavoris() }
            onResult(favoris)
        }
    }



    fun onDestroy() {
        job.cancel()
    }
}
