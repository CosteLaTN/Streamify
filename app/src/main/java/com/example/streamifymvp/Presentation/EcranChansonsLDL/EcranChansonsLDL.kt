package com.example.streamifymvp.Presentation.EcranChansonsLDL

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import com.example.streamifymvp.Presentation.Modele
import com.example.streamifymvp.R
import com.example.streamifymvp.Presentation.EcranChansonsLDL.Adapter.ChansonLDLAdapter
import kotlinx.coroutines.launch

class EcranChansonsLDL : Fragment(), IEcranChansonsLDL {

    private lateinit var adapter: ChansonLDLAdapter
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

        val modèle = Modele()
        présentateur = EcranChansonsLDLPresentateur(modèle)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            val chansons = présentateur.obtenirChansonsDeLaListeDeLecture(playlistId)
            adapter = ChansonLDLAdapter(chansons, présentateur, this@EcranChansonsLDL) { chanson, playlist ->
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
        }
    }

    override fun afficherChansons(chansons: List<Chanson>) {
        adapter.updateChansons(chansons)
    }

    override fun afficherDetailsListeDeLecture(playlist: ListeDeLecture) {
        val playlistTitreTextView = view?.findViewById<TextView>(R.id.textView)
        playlistTitreTextView?.text = playlist.nom
    }

    override fun afficherMessageAucuneChanson() {
        Toast.makeText(requireContext(), "Aucune chanson disponible dans cette playlist.", Toast.LENGTH_SHORT).show()
    }

    override fun afficherMessageErreur(message: String) {
        Toast.makeText(requireContext(), "Erreur : $message", Toast.LENGTH_SHORT).show()
    }
}
