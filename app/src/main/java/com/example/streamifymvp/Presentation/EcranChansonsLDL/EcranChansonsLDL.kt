package com.example.streamifymvp.presentation.Chansons

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streamifymvp.Presentation.Modele
import com.example.streamifymvp.R
import com.example.streamifymvp.SourceDeDonnees.ArtisteSourceBidon
import com.example.streamifymvp.Domaine.Service.ChansonService
import com.example.streamifymvp.SourceDeDonnees.ChansonSourceBidon
import com.example.streamifymvp.Domaine.Service.ArtisteService
import com.example.streamifymvp.Domaine.Service.ListeDeLectureService
import com.example.streamifymvp.Presentation.EcranChansonsLDL.Adapter.ChansonLDLAdapter
import com.example.streamifymvp.Presentation.EcranChansonsLDL.EcranChansonsLDLPresentateur
import com.google.android.material.bottomnavigation.BottomNavigationView

class EcranChansonsLDL : Fragment() {

    private lateinit var adapter: ChansonLDLAdapter
    private lateinit var navController: NavController
    private lateinit var présentateur: EcranChansonsLDLPresentateur
    private var playlistId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ecran_chansons_l_d_l, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistId = arguments?.getInt("playlistId") ?: -1

        if (playlistId == -1) {
            throw IllegalArgumentException("ID de la playlist manquant")
        }

        val modèle = Modele(
            ChansonService(ChansonSourceBidon.instance),
            ArtisteService(ArtisteSourceBidon()),
            ListeDeLectureService(ChansonSourceBidon.instance)
        )

        présentateur = EcranChansonsLDLPresentateur(modèle)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val chansons = présentateur.obtenirChansonsDeLaListeDeLecture(playlistId)
        adapter = ChansonLDLAdapter(chansons, présentateur) { chanson, playlist ->
            val bundle = Bundle().apply {
                putInt("chansonId", chanson.id)
                putInt("playlistId", playlistId)
            }
            Navigation.findNavController(view).navigate(R.id.action_ecranChansonsLDL_to_ecranLecture, bundle)
        }
        recyclerView.adapter = adapter

        // Mise à jour du titre de la playlist
        val playlistTitreTextView = view.findViewById<TextView>(R.id.textView)
        val playlist = présentateur.obtenirDetailsListeDeLecture(playlistId)
        playlistTitreTextView.text = playlist?.nom ?: "Ma liste de lecture"

        Log.d("EcranChansonsLDL", "Titre de la playlist : ${playlist?.nom}")

        navController = findNavController()

        val bottomNavigationView: BottomNavigationView = requireView().findViewById(R.id.bottomNavigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    Log.d("EcranChansonsLDL", "Navigating to Home")
                    navController.navigate(R.id.ecranAccueil)
                    true
                }
                R.id.nav_library -> {
                    Log.d("EcranChansonsLDL", "Navigating to Library")
                    navController.navigate(R.id.action_ecranChansonsLDL_to_ecranListeDeLecture)
                    true
                }
                R.id.nav_profile -> {
                    Log.d("EcranChansonsLDL", "Navigating to Profile")
                    navController.navigate(R.id.action_ecranChansonsLDL_to_profilVue)
                    true
                }
                else -> false
            }
        }
    }
}
