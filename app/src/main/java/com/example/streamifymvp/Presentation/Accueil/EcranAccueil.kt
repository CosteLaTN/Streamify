package com.example.streamify.Presentation.Accueil

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streamifymvp.Domaine.entitees.Artiste
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Presentation.Accueil.Adapter.NouveauteAdapter
import com.example.streamifymvp.Presentation.Accueil.Adapter.NouveauxArtistesAdapter
import com.example.streamifymvp.Presentation.Accueil.Adapter.ChansonAdapter
import com.example.streamifymvp.Presentation.Modele
import com.example.streamifymvp.R
import kotlinx.coroutines.launch

class EcranAccueil : Fragment(), AccueilVue {

    private lateinit var présentateur: AccueilPresentateur
    private lateinit var adaptateurChanson: ChansonAdapter
    private lateinit var nouveauteAdapter: NouveauteAdapter
    private lateinit var artistesAdapter: NouveauxArtistesAdapter

    private lateinit var recyclerViewChansons: RecyclerView
    private lateinit var recyclerViewNouveautes: RecyclerView
    private lateinit var recyclerViewNouveauxArtistes: RecyclerView

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

        val modèle = Modele()
        présentateur = AccueilPresentateur(this, modèle)

        adaptateurChanson = ChansonAdapter(emptyList(), emptyList()) { chanson ->
            Log.d("EcranAccueil", "Chanson cliquée: ${chanson.nom} avec ID: ${chanson.id}")
            val bundle = Bundle().apply {
                putInt("chansonId", chanson.id)
                putString("chansonNom", chanson.nom)
                putString("chansonArtiste", "Artiste inconnu") // Mise à jour lors du chargement des artistes
                putInt("artisteId", chanson.artisteId)
                putString("chansonImage", chanson.imageChanson)
                putString("chansonFichier", chanson.fichierAudio)
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

        // Chargement des données via coroutines
        lifecycleScope.launch {
            try {
                val chansons = modèle.obtenirToutesLesChansons().take(6)
                val artistes = modèle.obtenirTousLesArtistes()

                adaptateurChanson.updateChansons(chansons)
                artistesAdapter.updateArtistes(artistes)

                val nouveautés = modèle.obtenirNouveautés()
                nouveauteAdapter.updateChansons(nouveautés)
            } catch (e: Exception) {
                Log.e("EcranAccueil", "Erreur lors du chargement des données : ${e.message}")
                afficherMessageAucunRésultat()
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
