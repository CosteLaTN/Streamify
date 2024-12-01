package com.example.streamifymvp.Presentation.Lecture

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.R
import com.example.streamifymvp.SourceDeDonnees.SourceDeDonneeBidon

class MiniPlayerFragment(
    private val mediaPlayer: MediaPlayer,
    private val chansonActuelle: Chanson,
    private val navigationAction: Int
) : Fragment() {

    private lateinit var playPauseButton: ImageButton
    private lateinit var songTitle: TextView
    private lateinit var artistName: TextView
    lateinit var albumArt: ImageView
    private lateinit var progressBar: SeekBar
    private var isPlaying = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mini_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playPauseButton = view.findViewById(R.id.mini_player_play_pause_button)
        songTitle = view.findViewById(R.id.mini_player_song_title)
        artistName = view.findViewById(R.id.mini_player_artist_name)
        albumArt = view.findViewById(R.id.mini_player_album_art)
        progressBar = view.findViewById(R.id.mini_player_progress)

        // Mettre à jour les éléments de la vue avec les informations actuelles
        songTitle.text = chansonActuelle.nom
        val artiste = SourceDeDonneeBidon.instance.obtenirArtisteParId(chansonActuelle.artisteId)
        artistName.text = artiste?.pseudoArtiste ?: "Artiste Inconnu"
        albumArt.setImageResource(chansonActuelle.imageChanson)

        // Mettre à jour l'état du bouton play/pause
        playPauseButton.setImageResource(if (mediaPlayer.isPlaying) R.drawable.pause else R.drawable.play)

        setupListeners(view)
        setupProgressBar()
    }

    private fun setupListeners(view: View) {
        playPauseButton.setOnClickListener {
            try {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    playPauseButton.setImageResource(R.drawable.play)
                } else {
                    mediaPlayer.start()
                    playPauseButton.setImageResource(R.drawable.pause)
                }
                isPlaying = mediaPlayer.isPlaying
            } catch (e: IllegalStateException) {
                Log.e("MiniPlayerFragment", "Erreur d'état du MediaPlayer : ${e.message}")
            }
        }

        view.setOnClickListener {
            try {
                val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

                val bundle = Bundle()
                bundle.putInt("chansonId", chansonActuelle.id)
                bundle.putBoolean("isPlaying", isPlaying)  // Ajoutez l'état de lecture


                navController.navigate(R.id.ecranLecture, bundle)


                val miniPlayerContainer = requireActivity().findViewById<FrameLayout>(R.id.miniPlayerContainer)
                miniPlayerContainer.visibility = View.GONE
            } catch (e: IllegalStateException) {
                Log.e("MiniPlayerFragment", "Erreur lors de la navigation : ${e.message}")
                Toast.makeText(requireContext(), "Erreur lors de la navigation", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupProgressBar() {
        progressBar.max = mediaPlayer.duration
        progressBar.progress = mediaPlayer.currentPosition

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    if (mediaPlayer.isPlaying) {
                        progressBar.progress = mediaPlayer.currentPosition
                    }
                    handler.postDelayed(this, 1000)
                } catch (e: IllegalStateException) {
                    Log.e("MiniPlayerFragment", "Erreur d'état du MediaPlayer: ${e.message}")
                }
            }
        }, 1000)
    }

}
