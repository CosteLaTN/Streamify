package com.example.streamifymvp.Presentation.Historique

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
import com.example.streamifymvp.R
import com.example.streamifymvp.SourceDeDonnees.ISourceDeDonnee
import com.example.streamifymvp.SourceDeDonnees.SourceDeDonneeHTTP
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers

class HistoriqueVue : Fragment(), ContratVuePrésentateurHistorique.IHistoriqueVue {

    private lateinit var presentateur: ContratVuePrésentateurHistorique.IHistoriquePrésentateur
    private lateinit var navController: NavController
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ecran_historique, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listView = view.findViewById(R.id.history_list_view)
        navController = findNavController()


        val sourceDeDonnee: ISourceDeDonnee = SourceDeDonneeHTTP()
        presentateur = HistoriquePrésentateur(this, sourceDeDonnee, Dispatchers.IO)


        presentateur.chargerHistoriqueRecherche()

        configurerBottomNavigation(view)
    }

    private fun configurerBottomNavigation(view: View) {
        val bottomNavigationView: BottomNavigationView = view.findViewById(R.id.bottomNavigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> navigateToAccueil()
                R.id.nav_library -> navigateToLibrary()
                R.id.nav_search -> navigateToSearch()
                R.id.nav_profile -> navigateToProfile()
                else -> false
            }
        }
    }

    private fun navigateToAccueil(): Boolean {
        navController.navigate(R.id.ecranAccueil)
        return true
    }

    private fun navigateToLibrary(): Boolean {
        navController.navigate(R.id.action_historiqueVue_to_ecranListeDeLecture)
        return true
    }

    private fun navigateToSearch(): Boolean {
        navController.navigate(R.id.action_ecranAccueil_to_fragmentEcranRecherche)
        return true
    }

    private fun navigateToProfile(): Boolean {
        navController.navigate(R.id.action_historiqueVue_to_profilVue)
        return true
    }

    override fun afficherHistoriqueRecherche(historique: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, historique)
        listView.adapter = adapter
    }

    override fun afficherMessageErreur(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
