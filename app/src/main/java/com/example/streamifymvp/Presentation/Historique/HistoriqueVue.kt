import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.streamifymvp.Domaine.Service.HistoriqueService
import com.example.streamifymvp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoriqueVue : Fragment(), ContratVuePrésentateurHistorique.IHistoriqueVue {

    private lateinit var presentateur: ContratVuePrésentateurHistorique.IHistoriquePrésentateur
    private lateinit var navController: NavController
    private lateinit var listView: ListView
    private lateinit var historiqueService: HistoriqueService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ecran_historique, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        listView = view.findViewById(R.id.history_list_view)


        historiqueService = HistoriqueService(requireContext())

        chargerHistoriqueLocal()

        navController = findNavController()

        val bottomNavigationView: BottomNavigationView = requireView().findViewById(R.id.bottomNavigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    Log.d("HistoriqueVue", "Navigating to Home")
                    navController.navigate(R.id.ecranAccueil)
                    true
                }
                R.id.nav_library -> {
                    Log.d("HistoriqueVue", "Navigating to Library")
                    navController.navigate(R.id.action_historiqueVue_to_ecranListeDeLecture)
                    true
                }
                R.id.nav_profile -> {
                    Log.d("HistoriqueVue", "Navigating to Profile")
                    navController.navigate(R.id.action_historiqueVue_to_profilVue)
                    true
                }
                else -> false
            }
        }
    }

    private fun chargerHistoriqueLocal() {
        try {
            val historique = historiqueService.obtenirHistorique()
            afficherHistoriqueRecherche(historique)
        } catch (e: Exception) {
            afficherMessageErreur("Erreur lors du chargement de l'historique")
        }
    }

    override fun afficherHistoriqueRecherche(historique: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, historique)
        listView.adapter = adapter
    }

    override fun afficherMessageErreur(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }




}
