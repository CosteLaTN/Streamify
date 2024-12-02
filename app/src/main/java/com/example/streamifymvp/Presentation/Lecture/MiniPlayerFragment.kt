package com.example.streamifymvp.Presentation.Lecture

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.R
import kotlinx.coroutines.launch

class MiniPlayerFragment(
    private val mediaPlayer: MediaPlayer?,
    private val chansonActuelle: Chanson,
    private val navigationAction: Int
) : Fragment() {

    private lateinit var playPauseButton: ImageButton
    private lateinit var songTitle: TextView
    private lateinit var artistName: TextView
    lateinit var albumArt: ImageView
    private lateinit var progressBar: SeekBar
    private var isPlaying = true
    private val handler = Handler()

    private val miseAJourBarreDeProgression = object : Runnable {
        override fun run() {
            try {
                if (mediaPlayer != null && mediaPlayer.isPlaying) {
                    progressBar.progress = mediaPlayer.currentPosition / 1000
                }
                handler.postDelayed(this, 1000)
            } catch (e: IllegalStateException) {
                Log.e("MiniPlayerFragment", "Erreur d'état du MediaPlayer: ${e.message}")
            }
        }
    }

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

        // Mettre à jour les informations actuelles
        songTitle.text = chansonActuelle.nom
        lifecycleScope.launch {
            val artiste = chansonActuelle.artisteId // Charger dynamiquement si nécessaire
            artistName.text = artiste?.toString() ?: "Artiste Inconnu"
        }

        Glide.with(requireContext())
            .load(chansonActuelle.imageChanson)
            .placeholder(R.drawable.placeholder_image)
            .into(albumArt)

        // Initialiser les contrôles
        if (mediaPlayer != null) {
            playPauseButton.setImageResource(if (mediaPlayer.isPlaying) R.drawable.pause else R.drawable.play)
            progressBar.max = mediaPlayer.duration / 1000
            progressBar.progress = mediaPlayer.currentPosition / 1000
            setupListeners(view)
            handler.post(miseAJourBarreDeProgression)
        } else {
            afficherErreur("MediaPlayer non initialisé.")
        }
    }

    private fun setupListeners(view: View) {
        playPauseButton.setOnClickListener {
            try {
                if (mediaPlayer == null) {
                    afficherErreur("MediaPlayer non initialisé.")
                    return@setOnClickListener
                }

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
                bundle.putBoolean("isPlaying", isPlaying)

                navController.navigate(navigationAction, bundle)

                val miniPlayerContainer = requireActivity().findViewById<FrameLayout>(R.id.miniPlayerContainer)
                miniPlayerContainer.visibility = View.GONE
            } catch (e: IllegalStateException) {
                Log.e("MiniPlayerFragment", "Erreur lors de la navigation : ${e.message}")
                Toast.makeText(requireContext(), "Erreur lors de la navigation", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun afficherErreur(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        Log.e("MiniPlayerFragment", message)
        playPauseButton.setImageResource(R.drawable.play)
        progressBar.progress = 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(miseAJourBarreDeProgression)
    }
}
