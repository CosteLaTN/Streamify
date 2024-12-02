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

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var chansonActuelle: Chanson
    private var chansonsDuGenre: List<Chanson> = emptyList()
    private var isPlaying = false
    private var isRepeatEnabled = false
    private var currentSongIndex = 0
    private val handler = Handler()
    private lateinit var présentateur: LecturePresentateur

    private val miseAJourBarreDeProgression = object : Runnable {
        override fun run() {
            mediaPlayer?.let {
                progressBar.progress = it.currentPosition / 1000
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ecran_lecture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        val chansonId = arguments?.getInt("chansonId") ?: -1
        val isPlaying = arguments?.getBoolean("isPlaying", false) ?: false
        val currentPosition = arguments?.getInt("currentPosition", 0) ?: 0

        if (chansonId == -1) {
            Toast.makeText(requireContext(), "Erreur : Chanson introuvable", Toast.LENGTH_SHORT).show()
            return
        }

        présentateur = LecturePresentateur(Modele())
        lifecycleScope.launch(Dispatchers.IO) {
            chansonActuelle = présentateur.obtenirChansonParId(chansonId)
            chansonsDuGenre = présentateur.obtenirChansonsParGenre(chansonActuelle.genre)
            currentSongIndex = chansonsDuGenre.indexOf(chansonActuelle)

            withContext(Dispatchers.Main) {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(chansonActuelle.fichierAudio)
                        prepare()
                        seekTo(currentPosition)
                        if (isPlaying) start()
                    }
                } else {
                    mediaPlayer?.seekTo(currentPosition)
                    if (isPlaying) mediaPlayer?.start()
                }

                jouerChanson(chansonActuelle)
                setupListeners()
            }
        }
    }


    override fun jouerChanson(chanson: Chanson) {
        mediaPlayer?.release()
        handler.removeCallbacks(miseAJourBarreDeProgression)

        val audioUrl = chanson.fichierAudio // URL dynamique
        mediaPlayer = MediaPlayer().apply {
            setDataSource(audioUrl) // Charger depuis l'URL
            prepare()
            setOnCompletionListener {
                if (isRepeatEnabled) {
                    jouerChanson(chanson)
                } else {
                    jouerSuivante()
                }
            }
            start()
        }

        songTitle.text = chanson.nom
        lifecycleScope.launch {
            val artistePseudo = présentateur.obtenirPseudoArtiste(chanson.artisteId)
            artistName.text = artistePseudo
        }
        Glide.with(requireContext())
            .load(chanson.imageChanson) // Charger l'image depuis l'URL

            .into(imageAlbum)

        progressBar.max = (mediaPlayer?.duration ?: 0) / 1000
        progressBar.progress = 0
        isPlaying = true
        btnPlayPause.setImageResource(R.drawable.pause)

        handler.post(miseAJourBarreDeProgression)
    }

    override fun jouerSuivante() {
        currentSongIndex = (currentSongIndex + 1) % chansonsDuGenre.size
        chansonActuelle = chansonsDuGenre[currentSongIndex]
        jouerChanson(chansonActuelle)
    }

    override fun setupListeners() {
        btnPlayPause.setOnClickListener {
            isPlaying = !isPlaying
            btnPlayPause.setImageResource(if (isPlaying) R.drawable.pause else R.drawable.play)
            if (isPlaying) {
                mediaPlayer?.start()
                handler.post(miseAJourBarreDeProgression)
            } else {
                mediaPlayer?.pause()
                handler.removeCallbacks(miseAJourBarreDeProgression)
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
            btnRepeat.setImageResource(if (isRepeatEnabled) R.drawable.repeat_active else R.drawable.repeat)
        }

        btnShuffle.setOnClickListener {
            chansonsDuGenre = chansonsDuGenre.shuffled()
            currentSongIndex = 0
            chansonActuelle = chansonsDuGenre[currentSongIndex]
            jouerChanson(chansonActuelle)
        }

        btnArrowDown.setOnClickListener {
            val activity = requireActivity() as? MainActivity
            if (activity != null && mediaPlayer != null) {
                activity.ajouterMiniPlayerFragment(mediaPlayer!!, chansonActuelle)
                // Ne pas libérer le MediaPlayer ici, car il est utilisé par le MiniPlayer.
                Navigation.findNavController(requireView()).popBackStack()
            } else {
                Log.e("EcranLecture", "Erreur: MiniPlayer ou mediaPlayer est null")
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

                        // Force une nouvelle récupération des favoris après ajout
                        val updatedFavoris = présentateur.obtenirFavorisSuspendu()
                        val isFavorite = updatedFavoris?.chansons?.contains(chansonActuelle) == true

                        // Mise à jour de l'icône
                        withContext(Dispatchers.Main) {
                            btnFavorite.setImageResource(
                                if (isFavorite) R.drawable.favorited else R.drawable.favorite
                            )
                        }

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
            try {
                val favoris = présentateur.obtenirFavorisSuspendu()
                withContext(Dispatchers.Main) {
                    val isFavorite = favoris?.chansons?.contains(chansonActuelle) == true
                    Log.d("EcranLecture", "Mise à jour de l'icône des favoris. Est favori : $isFavorite")
                    btnFavorite.setImageResource(
                        if (isFavorite) R.drawable.favorited else R.drawable.favorite
                    )
                }
            } catch (e: Exception) {
                Log.e("EcranLecture", "Erreur lors de la mise à jour du bouton Favoris : ${e.message}")
            }
        }
    }



    override fun afficherListeDeLecturePourAjout() {
        lifecycleScope.launch {
            val playlists = présentateur.obtenirToutesLesListesDeLecture()
            withContext(Dispatchers.Main) {
                if (playlists.isEmpty()) {
                    Toast.makeText(requireContext(), "Aucune playlist disponible.", Toast.LENGTH_SHORT).show()
                    return@withContext
                }

                val nomsPlaylists: Array<CharSequence> = playlists.map { it.nom }.toTypedArray()
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

    override fun onDestroyView() {
        super.onDestroyView()
        if (!(requireActivity() as MainActivity).isMiniPlayerActive()) {
            mediaPlayer?.release()
        }
        handler.removeCallbacks(miseAJourBarreDeProgression)
    }

}
