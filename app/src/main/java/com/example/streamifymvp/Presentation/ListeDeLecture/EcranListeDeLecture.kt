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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import com.example.streamifymvp.Presentation.ListeDeLecture.Adapter.ListeDeLectureAdapter
import com.example.streamifymvp.Presentation.Modele
import com.example.streamifymvp.R

class EcranListeDeLecture : Fragment(), IEcranListeDeLecture {

    private lateinit var adapter: ListeDeLectureAdapter
    private lateinit var présentateur: IEcranListeDeLecturePresentateur

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

        présentateur = EcranListeDeLecturePresentateur(this, Modele())
        présentateur.chargerListesDeLecture()
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
                    présentateur.ajouterNouvellePlaylist(nomPlaylist)
                } else {
                    Toast.makeText(requireContext(), "Le nom de la playlist ne peut pas être vide.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Annuler", null)
            .create()
            .show()
    }

    override fun rafraichirListeDeLecture(playlists: List<ListeDeLecture>) {
        adapter.updateData(playlists)
    }

    override fun afficherMessageSucces(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun afficherMessageErreur(message: String) {
        Toast.makeText(requireContext(), "Erreur : $message", Toast.LENGTH_SHORT).show()
    }
}
