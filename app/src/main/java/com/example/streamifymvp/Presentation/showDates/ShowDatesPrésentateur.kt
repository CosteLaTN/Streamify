import com.example.streamifymvp.SourceDeDonnees.SourceDeDonneeBidon
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import com.example.streamifymvp.Domaine.Modeles.ShowDate

class ShowDatesPrésentateur(
    private val vue: ContratVuePrésentateurShowDates.IShowDatesVue
) : ContratVuePrésentateurShowDates.IShowDatesPrésentateur, CoroutineScope {

    private val service = SourceDeDonneeBidon()
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
                vue.afficherMessageErreur("Erreur lors du chargement des dates")
            }
        }
    }

    fun onDestroy() {
        job.cancel()
    }
}
