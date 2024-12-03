package com.example.streamifymvp.Presentation.Lecture

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
import com.example.streamifymvp.Presentation.Lecture.MediaPlayerManager.isPlayingCurrentChanson
import com.example.streamifymvp.R
import kotlinx.coroutines.launch

class MiniPlayerFragment(
    private val chansonActuelle: Chanson,
    private val navigationAction: Int
) : Fragment() {

    private lateinit var playPauseButton: ImageButton
    private lateinit var songTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var albumArt: ImageView
    private lateinit var progressBar: SeekBar

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

        // Mise à jour de l'interface utilisateur
        songTitle.text = chansonActuelle.nom
        artistName.text = chansonActuelle.artiste?.pseudoArtiste
        Glide.with(requireContext())
            .load(chansonActuelle.imageChanson)
            .placeholder(R.drawable.placeholder_image)
            .into(albumArt)

        MediaPlayerManager.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
            val isCurrentChansonPlaying = MediaPlayerManager.getMediaPlayer()?.isPlayingCurrentChanson(chansonActuelle) == true

            if (!isCurrentChansonPlaying && !isPlaying) {
                // Cacher le MiniPlayer si une autre chanson est jouée ou si aucune chanson n'est en lecture
                val miniPlayerContainer = requireActivity().findViewById<FrameLayout>(R.id.miniPlayerContainer)
                miniPlayerContainer.visibility = View.GONE
            } else {
                // Met à jour le bouton Play/Pause uniquement si la chanson correspond
                playPauseButton.setImageResource(if (isPlaying) R.drawable.pause else R.drawable.play)
            }
        }


        MediaPlayerManager.currentPosition.observe(viewLifecycleOwner) { position ->
            progressBar.progress = position / 1000
        }

        progressBar.max = MediaPlayerManager.getMediaPlayer()?.duration?.div(1000) ?: 0
        setupListeners(view)
    }

    private fun setupListeners(view: View) {
        playPauseButton.setOnClickListener {
            if (MediaPlayerManager.isPlaying.value == true) {
                MediaPlayerManager.pause()
            } else {
                MediaPlayerManager.resume()
            }
        }

        view.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            val bundle = Bundle().apply {
                putInt("chansonId", chansonActuelle.id)
            }
            navController.navigate(navigationAction, bundle)
            val miniPlayerContainer = requireActivity().findViewById<FrameLayout>(R.id.miniPlayerContainer)
            miniPlayerContainer.visibility = View.GONE
        }
    }
}


