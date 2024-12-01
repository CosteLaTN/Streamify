package com.example.streamifymvp.Presentation.Recherche

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streamifymvp.Domaine.Service.ArtisteService
import com.example.streamifymvp.Domaine.Service.ChansonService
import com.example.streamifymvp.Domaine.Service.ListeDeLectureService
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Presentation.Accueil.Adapter.ChansonAdapter
import com.example.streamifymvp.Presentation.IModele
import com.example.streamifymvp.Presentation.Modele
import com.example.streamifymvp.R
import com.example.streamifymvp.SourceDeDonnees.HistoriqueDatabaseHelper
import com.example.streamifymvp.SourceDeDonnees.SourceDeDonneeBidon
import com.google.android.material.bottomnavigation.BottomNavigationView

class FragmentEcranRecherche : Fragment(), RechercheContrat.IRechercheVue {

    private lateinit var historiqueDbHelper: HistoriqueDatabaseHelper
    private lateinit var searchView: SearchView
    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var presentateur: RecherchePresentateur
    private lateinit var chansonAdapter: ChansonAdapter
    private lateinit var modele: IModele
    private lateinit var historiqueListView: ListView
    private lateinit var historiqueAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ecran_recherche, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chansonService = ChansonService(SourceDeDonneeBidon.instance)
        val artisteService = ArtisteService(SourceDeDonneeBidon.instance)
        val listeDeLectureService = ListeDeLectureService(SourceDeDonneeBidon.instance)
        modele = Modele(chansonService, artisteService, listeDeLectureService)
        historiqueDbHelper = HistoriqueDatabaseHelper(requireContext())

        presentateur = RecherchePresentateur(modele, this)

        searchView = view.findViewById(R.id.searchView)
        resultsRecyclerView = view.findViewById(R.id.results_recycler_view)
        resultsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        historiqueListView = view.findViewById(R.id.historique_list_view)

        setupHistoriqueList()
        setupSearchListener()
        setupBottomNavigation(view)
    }

    @SuppressLint("Range")
    private fun setupHistoriqueList() {
        val historique = historiqueDbHelper.readableDatabase.query(
            HistoriqueDatabaseHelper.TABLE_HISTORIQUE,
            arrayOf(HistoriqueDatabaseHelper.COLUMN_RECHERCHE),
            null,
            null,
            null,
            null,
            "${HistoriqueDatabaseHelper.COLUMN_ID} DESC"
        ).use { cursor ->
            generateSequence { if (cursor.moveToNext()) cursor else null }
                .map { it.getString(it.getColumnIndex(HistoriqueDatabaseHelper.COLUMN_RECHERCHE)) }
                .toList()
        }

        historiqueAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, historique)
        historiqueListView.adapter = historiqueAdapter

        // Lorsque l'utilisateur clique sur une recherche précédente, la recherche est effectuée
        historiqueListView.setOnItemClickListener { _, _, position, _ ->
            val query = historiqueAdapter.getItem(position)
            searchView.setQuery(query, true)
        }

        // Masquer la liste par défaut
        historiqueListView.visibility = View.GONE
    }

    private fun setupSearchListener() {
        // Affiche l'historique lorsque l'utilisateur clique sur la barre de recherche
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                historiqueListView.visibility = View.VISIBLE
            } else {
                historiqueListView.visibility = View.GONE
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Masquer l'historique et enregistrer la recherche
                historiqueListView.visibility = View.GONE
                if (!query.isNullOrBlank()) {
                    sauvegarderRecherche(query)
                    presentateur.effectuerRecherche(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    historiqueListView.visibility = View.GONE
                }
                return true
            }
        })
    }

    private fun sauvegarderRecherche(recherche: String) {
        val db = historiqueDbHelper.writableDatabase
        val values = ContentValues().apply {
            put(HistoriqueDatabaseHelper.COLUMN_RECHERCHE, recherche)
        }
        db.insert(HistoriqueDatabaseHelper.TABLE_HISTORIQUE, null, values)
    }

    private fun setupBottomNavigation(view: View) {
        val navController = findNavController()
        val bottomNavigationView: BottomNavigationView = view.findViewById(R.id.bottomNavigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.action_fragmentEcranRecherche_to_ecranAccueil)
                    true
                }
                R.id.nav_library -> {
                    navController.navigate(R.id.action_fragmentEcranRecherche_to_ecranListeDeLecture)
                    true
                }
                R.id.nav_profile -> {
                    navController.navigate(R.id.action_fragmentEcranRecherche_to_profilVue)
                    true
                }
                else -> false
            }
        }
    }

    override fun afficherResultats(resultats: List<Chanson>) {
        chansonAdapter = ChansonAdapter(resultats, modele.obtenirTousLesArtistes()) { chanson ->
            val bundle = Bundle().apply {
                putInt("chansonId", chanson.id)
            }
            findNavController().navigate(R.id.action_fragmentEcranRecherche_to_ecranLecture, bundle)
        }
        resultsRecyclerView.adapter = chansonAdapter
    }

    override fun afficherMessageAucunResultat() {
        Toast.makeText(requireContext(), "Aucun résultat trouvé", Toast.LENGTH_SHORT).show()
    }
}
