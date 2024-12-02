package com.example.streamifymvp.Presentation.showDates

import android.util.Log
import com.example.streamifymvp.SourceDeDonnees.SourceDeDonneeBidon
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ShowDatesPrésentateur(
    private val vue: ContratVuePrésentateurShowDates.IShowDatesVue,
    private val service: SourceDeDonneeBidon = SourceDeDonneeBidon()
) : ContratVuePrésentateurShowDates.IShowDatesPrésentateur, CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun chargerDates() {
        launch {
            try {
                val dates = withContext(Dispatchers.IO) { service.obtenirToutesLesDatesDeShow() }
                if (dates.isNotEmpty()) {
                    vue.afficherDates(dates)
                } else {
                    vue.afficherMessageErreur("Aucune date disponible.")
                }
            } catch (e: Exception) {
                Log.e("ShowDatesPrésentateur", "Erreur lors du chargement des dates", e)
                vue.afficherMessageErreur("Erreur lors du chargement des dates")
            }
        }
    }

    fun onDestroy() {
        job.cancel()
    }
}
