package com.example.streamify.Presentation.Accueil

import ChansonAdapter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streamifymvp.Domaine.entitees.Artiste
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.Service.ArtisteService
import com.example.streamifymvp.Domaine.Service.ListeDeLectureService
import com.example.streamifymvp.Presentation.Accueil.Adapter.NouveauteAdapter
import com.example.streamifymvp.Presentation.Accueil.Adapter.NouveauxArtistesAdapter
import com.example.streamifymvp.R
import com.example.streamifymvp.Domaine.Service.ChansonService
import com.example.streamifymvp.Domaine.Service.HistoriqueService
import com.example.streamifymvp.Presentation.Modele
import com.example.streamifymvp.SourceDeDonnees.ISourceDeDonnee
import com.example.streamifymvp.SourceDeDonnees.SourceDeDonneeBidon
import com.google.android.material.bottomnavigation.BottomNavigationView

class EcranAccueil : Fragment(), AccueilVue {

    private lateinit var présentateur: AccueilPresentateur
    private lateinit var adaptateurChanson: ChansonAdapter
    private lateinit var nouveauteAdapter: NouveauteAdapter
    private lateinit var artistesAdapter: NouveauxArtistesAdapter

    private lateinit var recyclerViewChansons: RecyclerView
    private lateinit var recyclerViewNouveautes: RecyclerView
    private lateinit var recyclerViewNouveauxArtistes: RecyclerView
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ecran_accueil, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewChansons = view.findViewById(R.id.recyclerViewChansons)
        recyclerViewNouveautes = view.findViewById(R.id.recyclerViewNouveautes)
        recyclerViewNouveauxArtistes = view.findViewById(R.id.recyclerViewNouveauxArtistes)
        searchView = view.findViewById(R.id.searchView)

        val historiqueService = HistoriqueService(requireContext())

        val chansonService = ChansonService(SourceDeDonneeBidon.instance)
        val artisteService = ArtisteService(SourceDeDonneeBidon())
        val listeDeLectureService = ListeDeLectureService(SourceDeDonneeBidon.instance)
        val modèle = Modele(chansonService, artisteService, listeDeLectureService)
        présentateur = AccueilPresentateur(this, modèle)
        lateinit var navController: NavController

        val chansonsLimite = modèle.obtenirToutesLesChansons().take(6)
        val artistes = modèle.obtenirTousLesArtistes()


        adaptateurChanson = ChansonAdapter(chansonsLimite, artistes) { chanson ->
            Log.d("EcranAccueil", "Chanson cliquée: ${chanson.nom} avec ID: ${chanson.id}")
            val artiste = artistes.find { it.id == chanson.artisteId }
            val bundle = Bundle().apply {
                putInt("chansonId", chanson.id)
                putString("chansonNom", chanson.nom)
                putString("chansonArtiste", artiste?.pseudoArtiste ?: "Artiste inconnu")
                putInt("artisteId", chanson.artisteId)
                putInt("chansonImage", chanson.imageChanson)
                putInt("chansonFichier", chanson.fichierAudio)
            }
            Navigation.findNavController(view).navigate(R.id.actionEcranAccueilToEcranLecture, bundle)
        }
        recyclerViewChansons.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerViewChansons.adapter = adaptateurChanson

        nouveauteAdapter = NouveauteAdapter(emptyList())
        recyclerViewNouveautes.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewNouveautes.adapter = nouveauteAdapter

        artistesAdapter = NouveauxArtistesAdapter(emptyList())
        recyclerViewNouveauxArtistes.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewNouveauxArtistes.adapter = artistesAdapter

        présentateur.chargerAccueil()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                présentateur.rechercherChansons(newText ?: "")
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    historiqueService.ajouterRecherche(query)
                }
                return false
            }
        })

        navController = findNavController()

        val bottomNavigationView: BottomNavigationView = requireView().findViewById(R.id.bottomNavigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    Log.d("EcranAccueil", "Navigating to Home")
                    navController.navigate(R.id.ecranAccueil)
                    true
                }
                R.id.nav_library -> {
                    Log.d("EcranAccueil", "Navigating to Library")
                    navController.navigate(R.id.action_ecranAccueil_to_ecranListeDeLecture)
                    true
                }
                R.id.nav_library -> {
                    Log.d("EcranAccueil", "Navigating to Library")
                    navController.navigate(R.id.ecranListeDeLecture)
                    true
                }
                R.id.nav_profile -> {
                    Log.d("EcranAccueil", "Navigating to Profile")
                    navController.navigate(R.id.action_ecranAccueil_to_profilVue)
                    true
                }
                R.id.nav_profile -> {
                    Log.d("EcranAccueil", "Navigating to Profile")
                    navController.navigate(R.id.profilVue)
                    true
                }
                else -> false
            }
        }
    }

    override fun afficherChansons(chansons: List<Chanson>) {
        adaptateurChanson.updateChansons(chansons.take(6))
    }

    override fun afficherNouveautés(nouveautés: List<Chanson>) {
        nouveauteAdapter.updateChansons(nouveautés)
    }

    override fun afficherNouveauxArtistes(artistes: List<Artiste>) {
        artistesAdapter.updateArtistes(artistes)
    }

    override fun afficherMessageAucunRésultat() {
        Toast.makeText(requireContext(), "Aucun résultat trouvé", Toast.LENGTH_SHORT).show()
    }
}
