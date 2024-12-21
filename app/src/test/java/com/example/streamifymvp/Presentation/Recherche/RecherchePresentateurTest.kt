import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Presentation.Recherche.RechercheContrat
import com.example.streamifymvp.Presentation.Recherche.RecherchePresentateur
import com.example.streamifymvp.Presentation.Modele
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class RecherchePresentateurTest {

    private lateinit var vue: RechercheContrat.IRechercheVue
    private lateinit var modele: Modele
    private lateinit var présentateur: RecherchePresentateur

    @Before
    fun setup() {
        vue = mock()
        modele = mock()
        présentateur = RecherchePresentateur(modele, vue)
    }

    @Test
    fun `etant donne qu'on effectue une Recherche affiche resultats quand non vide`() = runBlocking {
        val resultats = listOf(Chanson(1,"Test","2020-01-01","Rock","X","Y","img","audio",null,null))
        whenever(modele.rechercherChansons("test")).thenReturn(resultats)

        présentateur.effectuerRecherche("test")

        verify(vue).afficherResultats(resultats)
    }

    @Test
    fun `effectuerRecherche affiche aucun resultat quand vide`() = runBlocking {
        whenever(modele.rechercherChansons("test")).thenReturn(emptyList())

        présentateur.effectuerRecherche("test")

        verify(vue).afficherMessageAucunResultat()
    }

    @Test
    fun `effectuerRecherche affiche erreur quand exception`() = runBlocking {
        whenever(modele.rechercherChansons("test")).thenThrow(RuntimeException("erreur"))

        présentateur.effectuerRecherche("test")

        verify(vue).afficherMessageAucunResultat()
    }
}
