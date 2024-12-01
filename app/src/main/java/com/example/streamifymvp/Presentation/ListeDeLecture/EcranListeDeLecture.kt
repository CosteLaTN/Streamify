package com.example.streamifymvp.Presentation.ListeDeLecture

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import com.example.streamifymvp.Presentation.Lecture.IEcranLecture
import com.example.streamifymvp.Presentation.ListeDeLecture.Adapter.ListeDeLectureAdapter
import com.example.streamifymvp.Presentation.ListeDeLecture.IEcranListeDeLecture
import com.example.streamifymvp.R
import com.example.streamifymvp.SourceDeDonnees.SourceDeDonneeBidon
import com.google.android.material.bottomnavigation.BottomNavigationView

class EcranListeDeLecture : Fragment(), IEcranListeDeLecture {

    private lateinit var adapter: ListeDeLectureAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ecran_liste_de_lecture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerLDL)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val navController = findNavController()

        adapter = ListeDeLectureAdapter(mutableListOf()) { playlist ->
            val bundle = Bundle().apply {
                putInt("playlistId", playlist.id)
            }
            navController.navigate(R.id.action_ecranListeDeLecture_to_ecranChansonsLDL, bundle)
        }
        recyclerView.adapter = adapter

        val creerLDLButton = view.findViewById<ImageButton>(R.id.CréerLDL)
        creerLDLButton.setOnClickListener {
            afficherDialogCreationPlaylist()
        }

        val bottomNavigationView: BottomNavigationView = view.findViewById(R.id.bottomNavigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    Log.d("EcranAccueil", "Navigating to Home")
                    navController.navigate(R.id.action_ecranListeDeLecture_to_ecranAccueil)
                    true
                }
                R.id.nav_library -> {
                    Log.d("EcranAccueil", "Navigating to Library")
                    navController.navigate(R.id.action_ecranListeDeLecture_self)
                    true
                }
                R.id.nav_search -> {
                    Log.d("EcranAccueil", "Navigating to Search")
                    navController.navigate(R.id.action_ecranListeDeLecture_to_fragmentEcranRecherche)
                    true
                }
                R.id.nav_profile -> {
                    Log.d("EcranAccueil", "Navigating to Profile")
                    navController.navigate(R.id.action_ecranListeDeLecture_to_profilVue)
                    true
                }
                else -> false
            }
        }

        rafraichirListeDeLecture()
    }

    override fun afficherDialogCreationPlaylist() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.formulaire_creation_playlist, null)

        builder.setView(dialogView)
            .setTitle("Créer une Playlist")
            .setPositiveButton("Confirmer") { _, _ ->
                val editTextNom = dialogView.findViewById<EditText>(R.id.nomPlaylistCreation)
                val nomPlaylist = editTextNom.text.toString()
                if (nomPlaylist.isNotEmpty()) {
                    ajouterNouvellePlaylist(nomPlaylist)
                } else {
                    Toast.makeText(requireContext(), "Le nom de la playlist ne peut pas être vide.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Annuler", null)
            .create()
            .show()
    }

    override fun ajouterNouvellePlaylist(nom: String) {
        val nouvellePlaylist = ListeDeLecture(
            id = System.currentTimeMillis().toInt(),
            nom = nom,
            chansons = mutableListOf()
        )
        val chansonSource = SourceDeDonneeBidon.instance
        chansonSource.ajouterPlaylist(nouvellePlaylist)
        rafraichirListeDeLecture()
    }

    override fun rafraichirListeDeLecture() {
        val chansonSource = SourceDeDonneeBidon.instance
        val playlists = chansonSource.obtenirToutesLesListesDeLecture()
        adapter.updateData(playlists)
    }
}
