package com.example.streamifymvp.Presentation.Recherche

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Presentation.Accueil.Adapter.ChansonAdapter
import com.example.streamifymvp.Presentation.IModele
import com.example.streamifymvp.Presentation.Modele
import com.example.streamifymvp.R
import com.example.streamifymvp.SourceDeDonnees.HistoriqueDatabaseHelper
import kotlinx.coroutines.*

class FragmentEcranRecherche : Fragment(), RechercheContrat.IRechercheVue {

    private lateinit var historiqueDbHelper: HistoriqueDatabaseHelper
    private lateinit var searchView: SearchView
    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var presentateur: RecherchePresentateur
    private lateinit var chansonAdapter: ChansonAdapter
    private lateinit var modele: IModele
    private lateinit var historiqueListView: ListView
    private lateinit var historiqueAdapter: ArrayAdapter<String>

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ecran_recherche, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        modele = Modele()
        historiqueDbHelper = HistoriqueDatabaseHelper(requireContext())
        presentateur = RecherchePresentateur(modele, this)

        searchView = view.findViewById(R.id.searchView)
        resultsRecyclerView = view.findViewById(R.id.results_recycler_view)
        resultsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        historiqueListView = view.findViewById(R.id.historique_list_view)

        setupHistoriqueList()
        setupSearchListener()
    }

    @SuppressLint("Range")
    private fun setupHistoriqueList() {
        val historique = coroutineScope.async(Dispatchers.IO) {
            historiqueDbHelper.readableDatabase.query(
                HistoriqueDatabaseHelper.TABLE_HISTORIQUE,
                arrayOf(HistoriqueDatabaseHelper.COLUMN_RECHERCHE),
                null,
                null,
                null,
                null,
                "${HistoriqueDatabaseHelper.COLUMN_ID} DESC"
            ).use { cursor ->
                generateSequence { if (cursor.moveToNext()) cursor else null }
                    .map { it.getString(cursor.getColumnIndex(HistoriqueDatabaseHelper.COLUMN_RECHERCHE)) }
                    .toList()
            }
        }

        coroutineScope.launch {
            val data = historique.await()
            historiqueAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, data)
            historiqueListView.adapter = historiqueAdapter

            historiqueListView.setOnItemClickListener { _, _, position, _ ->
                val query = historiqueAdapter.getItem(position)
                searchView.setQuery(query, true)
            }

            historiqueListView.visibility = View.GONE
        }
    }

    private fun setupSearchListener() {
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            historiqueListView.visibility = if (hasFocus) View.VISIBLE else View.GONE
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                historiqueListView.visibility = View.GONE
                query?.let {
                    sauvegarderRecherche(it)
                    presentateur.effectuerRecherche(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                historiqueListView.visibility = View.GONE
                return true
            }
        })
    }

    private fun sauvegarderRecherche(recherche: String) {
        coroutineScope.launch(Dispatchers.IO) {
            val db = historiqueDbHelper.writableDatabase
            val values = ContentValues().apply {
                put(HistoriqueDatabaseHelper.COLUMN_RECHERCHE, recherche)
            }
            db.insert(HistoriqueDatabaseHelper.TABLE_HISTORIQUE, null, values)
        }
    }

    override fun afficherResultats(resultats: List<Chanson>) {
        lifecycleScope.launch {
            val artistes = withContext(Dispatchers.IO) { modele.obtenirTousLesArtistes() }

            chansonAdapter = ChansonAdapter(resultats, artistes) { chanson ->
                val bundle = Bundle().apply {
                    putInt("chansonId", chanson.id)
                    putString("chansonImageUrl", chanson.imageChanson)
                    putString("chansonAudioUrl", chanson.fichierAudio)
                }
                findNavController().navigate(R.id.action_fragmentEcranRecherche_to_ecranLecture, bundle)
            }
            resultsRecyclerView.adapter = chansonAdapter
        }
    }


    override fun afficherMessageAucunResultat() {
        Toast.makeText(requireContext(), "Aucun résultat trouvé.", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineScope.cancel()
    }
}
