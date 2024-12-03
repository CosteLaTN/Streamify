package com.example.streamifymvp.Presentation.Lecture

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.MainActivity
import com.example.streamifymvp.Presentation.Modele
import com.example.streamifymvp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EcranLecture : Fragment(), IEcranLecture {

    // Déclarations des boutons et vues
    private lateinit var progressBar: SeekBar
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnPrevious: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var btnShuffle: ImageButton
    private lateinit var btnRepeat: ImageButton
    private lateinit var btnFavorite: ImageButton
    private lateinit var btnArrowDown: ImageButton
    private lateinit var songTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var imageAlbum: ImageView
    private lateinit var btnPlaylist: ImageButton

    private lateinit var chansonActuelle: Chanson
    private var chansonsDuGenre: List<Chanson> = emptyList()
    private var currentSongIndex = 0
    private var isRepeatEnabled = false
    private val handler = Handler()
    private lateinit var présentateur: LecturePresentateur

    private val miseAJourBarreDeProgression = object : Runnable {
        override fun run() {
            progressBar.progress = MediaPlayerManager.getCurrentPosition() / 1000
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ecran_lecture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialisation des vues
        progressBar = view.findViewById(R.id.progressBar)
        btnPlayPause = view.findViewById(R.id.btnPlayPause)
        btnPrevious = view.findViewById(R.id.btnPrevious)
        btnNext = view.findViewById(R.id.btnNext)
        btnShuffle = view.findViewById(R.id.btnShuffle)
        btnRepeat = view.findViewById(R.id.btnRepeat)
        btnFavorite = view.findViewById(R.id.btnFavorite)
        btnArrowDown = view.findViewById(R.id.btnArrowDown)
        btnPlaylist = view.findViewById(R.id.btnPlaylist)
        songTitle = view.findViewById(R.id.songTitle)
        artistName = view.findViewById(R.id.artistName)
        imageAlbum = view.findViewById(R.id.imageAlbum)

        // Récupération des arguments
        val chansonId = arguments?.getInt("chansonId") ?: -1
        val playlistId = arguments?.getInt("playlistId", -1) ?: -1 // Nouvelle source
        val source = if (playlistId != -1) "playlist" else "genre"
        if (chansonId == -1) {
            Toast.makeText(requireContext(), "Erreur : Chanson introuvable", Toast.LENGTH_SHORT).show()
            return
        }

        présentateur = LecturePresentateur(Modele())
        lifecycleScope.launch(Dispatchers.IO) {
            chansonActuelle = présentateur.obtenirChansonParId(chansonId)
            chansonsDuGenre = if (playlistId != -1) {
                présentateur.obtenirChansonsDeLaListeDeLecture(playlistId)
            } else {
                présentateur.obtenirChansonsParGenre(chansonActuelle.genre)
            }
            currentSongIndex = chansonsDuGenre.indexOf(chansonActuelle)

            withContext(Dispatchers.Main) {
                jouerChanson(chansonActuelle)
                setupListeners()
                observerMediaPlayerState()
            }
        }
    }

    private fun observerMediaPlayerState() {
        MediaPlayerManager.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
            btnPlayPause.setImageResource(if (isPlaying) R.drawable.pause else R.drawable.play)
        }

        MediaPlayerManager.currentPosition.observe(viewLifecycleOwner) { position ->
            progressBar.progress = position / 1000
        }
    }

    override fun jouerChanson(chanson: Chanson) {
        MediaPlayerManager.playChanson(chanson)
        songTitle.text = chanson.nom
        lifecycleScope.launch {
            val artistePseudo = présentateur.obtenirPseudoArtiste(chanson.artisteId)
            artistName.text = artistePseudo
        }
        Glide.with(requireContext())
            .load(chanson.imageChanson)
            .into(imageAlbum)

        if (MediaPlayerManager.isMediaPlayerValid()) {
            val mediaPlayer = MediaPlayerManager.getMediaPlayer()
            if (mediaPlayer != null) {
                progressBar.max = mediaPlayer.duration / 1000
                handler.post(miseAJourBarreDeProgression)
            }
        } else {
            Log.e("EcranLecture", "Erreur: MediaPlayer est invalide.")
            Toast.makeText(requireContext(), "Erreur lors de la lecture", Toast.LENGTH_SHORT).show()
        }
    }



    override fun jouerSuivante() {
        currentSongIndex = (currentSongIndex + 1) % chansonsDuGenre.size
        if (chansonsDuGenre.isNotEmpty()) {
            chansonActuelle = chansonsDuGenre[currentSongIndex]
            jouerChanson(chansonActuelle)
        } else {
            Log.e("EcranLecture", "Aucune chanson disponible dans la liste.")
            Toast.makeText(requireContext(), "Aucune chanson disponible à jouer.", Toast.LENGTH_SHORT).show()
        }
    }


    override fun setupListeners() {
        btnPlayPause.setOnClickListener {
            if (MediaPlayerManager.isPlaying.value == true) {
                MediaPlayerManager.pause()
            } else {
                MediaPlayerManager.resume()
            }
        }

        btnNext.setOnClickListener { jouerSuivante() }
        btnPrevious.setOnClickListener {
            currentSongIndex = if (currentSongIndex > 0) currentSongIndex - 1 else chansonsDuGenre.size - 1
            chansonActuelle = chansonsDuGenre[currentSongIndex]
            jouerChanson(chansonActuelle)
        }

        btnRepeat.setOnClickListener {
            isRepeatEnabled = !isRepeatEnabled
            MediaPlayerManager.setRepeatMode(isRepeatEnabled)
            btnRepeat.setImageResource(if (isRepeatEnabled) R.drawable.repeat_active else R.drawable.repeat)
        }

        btnShuffle.setOnClickListener {
            chansonsDuGenre = chansonsDuGenre.shuffled()
            currentSongIndex = 0
            chansonActuelle = chansonsDuGenre[currentSongIndex]
            jouerChanson(chansonActuelle)
        }

        btnArrowDown.setOnClickListener {
            val activity = requireActivity() as MainActivity
            val mediaPlayer = MediaPlayerManager.getMediaPlayer()
            if (mediaPlayer != null) {
                activity.ajouterMiniPlayerFragment(mediaPlayer, chansonActuelle)
                Navigation.findNavController(requireView()).popBackStack()
            } else {
                Log.e("EcranLecture", "Erreur: MediaPlayer est null")
                Toast.makeText(requireContext(), "Erreur lors de l'affichage du mini-player", Toast.LENGTH_SHORT).show()
            }
        }

        btnPlaylist.setOnClickListener { afficherListeDeLecturePourAjout() }

        btnFavorite.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val favoris = présentateur.obtenirFavorisSuspendu()
                    val isAlreadyFavorite = favoris?.chansons?.contains(chansonActuelle) == true

                    if (isAlreadyFavorite) {
                        Toast.makeText(requireContext(), "Cette chanson est déjà dans les favoris.", Toast.LENGTH_SHORT).show()
                    } else {
                        présentateur.ajouterAuxFavoris(chansonActuelle)
                        updateFavoriteButtonIcon()
                        Toast.makeText(requireContext(), "Ajoutée aux favoris.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("EcranLecture", "Erreur lors de l'ajout aux favoris : ${e.message}")
                    Toast.makeText(requireContext(), "Une erreur est survenue.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun updateFavoriteButtonIcon() {
        lifecycleScope.launch {
            val favoris = présentateur.obtenirFavorisSuspendu()
            withContext(Dispatchers.Main) {
                val isFavorite = favoris?.chansons?.contains(chansonActuelle) == true
                btnFavorite.setImageResource(if (isFavorite) R.drawable.favorited else R.drawable.favorite)
            }
        }
    }

    override fun afficherListeDeLecturePourAjout() {
        lifecycleScope.launch {
            val playlists = présentateur.obtenirToutesLesListesDeLecture()
            withContext(Dispatchers.Main) {
                if (playlists.isEmpty()) {
                    Toast.makeText(requireContext(), "Aucune playlist disponible.", Toast.LENGTH_SHORT).show()
                } else {
                    val nomsPlaylists = playlists.map { it.nom }.toTypedArray()
                    AlertDialog.Builder(requireContext())
                        .setTitle("Ajouter à une playlist")
                        .setItems(nomsPlaylists) { _, which ->
                            val playlistSelectionnee = playlists[which]
                            lifecycleScope.launch {
                                présentateur.ajouterChansonAPlaylist(playlistSelectionnee.id, chansonActuelle)
                                Toast.makeText(requireContext(), "Ajoutée à ${playlistSelectionnee.nom}.", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .setNegativeButton("Annuler", null)
                        .show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(miseAJourBarreDeProgression)
    }
}


