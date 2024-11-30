package com.example.streamifymvp.Presentation.Lecture

import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Presentation.Modele
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import kotlinx.coroutines.*

class LecturePresentateur (
    private val modele: Modele,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ILecturePresentateur {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun ajouterAuxFavoris(chanson: Chanson) {
        scope.launch {
            withContext(ioDispatcher) {
                modele.ajouterChansonAuxFavoris(chanson)
            }
        }
    }

    override fun obtenirFavoris(onResult: (ListeDeLecture?) -> Unit) {
        scope.launch {
            val favoris = withContext(ioDispatcher) { modele.obtenirFavoris() }
            onResult(favoris)
        }
    }

    fun onDestroy() {
        job.cancel()
    }
}
