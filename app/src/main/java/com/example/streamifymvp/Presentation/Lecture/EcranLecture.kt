package com.example.streamifymvp.Presentation.Lecture

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.example.streamifymvp.Domaine.Modeles.Chanson
import com.example.streamifymvp.Domaine.Service.ArtisteService
import com.example.streamifymvp.R
import com.example.streamifymvp.SourceDeDonnees.ArtisteSourceBidon
import com.example.streamifymvp.SourceDeDonnees.ChansonSourceBidon
import com.example.streamifymvp.Presentation.Modele
import com.example.streamifymvp.Domaine.Service.ChansonService
import com.example.streamifymvp.Domaine.Service.ListeDeLectureService

class EcranLecture : Fragment() {

    private lateinit var progressBar: SeekBar
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnPrevious: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var btnShuffle: ImageButton
    private lateinit var btnRepeat: ImageButton
    private lateinit var btnArrowDown: ImageButton
    private lateinit var btnFavorite: ImageButton
    private lateinit var btnPlaylist: ImageButton
    private lateinit var songTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var imageAlbum: ImageView

    private var isPlaying = false
    private var currentSongIndex = 0
    private var isRepeatEnabled = false
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var chansonsDuGenre: List<Chanson>
    private lateinit var chansonActuelle: Chanson

    private val handler = Handler()
    private val chansonSourceBidon = ChansonSourceBidon.instance
    private lateinit var présentateur: LecturePresentateur

    private val miseAJourBarreDeProgression = object : Runnable {
        override fun run() {
            if (isPlaying && mediaPlayer != null) {
                val currentPosition = mediaPlayer?.currentPosition ?: 0
                progressBar.progress = currentPosition / 1000
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
        btnArrowDown = view.findViewById(R.id.btnArrowDown)
        btnFavorite = view.findViewById(R.id.btnFavorite)
        btnPlaylist = view.findViewById(R.id.btnPlaylist)
        songTitle = view.findViewById(R.id.songTitle)
        artistName = view.findViewById(R.id.artistName)
        imageAlbum = view.findViewById(R.id.imageAlbum)

        val chansonService = ChansonService(ChansonSourceBidon.instance)
        val artisteService = ArtisteService(ArtisteSourceBidon())
        val listeDeLectureService = ListeDeLectureService(ChansonSourceBidon.instance)
        val modele = Modele(chansonService, artisteService, listeDeLectureService)
        présentateur = LecturePresentateur(modele)

        val chansonId = arguments?.getInt("chansonId") ?: -1
        chansonActuelle = chansonSourceBidon.obtenirToutesLesChansons().find { it.id == chansonId } ?: throw IllegalArgumentException("Chanson introuvable")
        chansonsDuGenre = chansonSourceBidon.obtenirToutesLesChansons().filter { it.genre == chansonActuelle.genre }
        currentSongIndex = chansonsDuGenre.indexOf(chansonActuelle)

        setupListeners()
        jouerChanson(chansonActuelle)
    }

    private fun jouerChanson(chanson: Chanson) {
        mediaPlayer?.release()
        handler.removeCallbacks(miseAJourBarreDeProgression)

        mediaPlayer = MediaPlayer.create(requireContext(), chanson.fichierAudio)
        mediaPlayer?.setOnCompletionListener {
            if (isRepeatEnabled) {
                jouerChanson(chanson)
            } else {
                jouerSuivante()
            }
        }
        mediaPlayer?.start()

        songTitle.text = chanson.nom
        artistName.text = chansonSourceBidon.obtenirFavoris()?.nom ?: "Inconnu"
        imageAlbum.setImageResource(chanson.imageChanson)

        progressBar.max = (mediaPlayer?.duration ?: 0) / 1000
        progressBar.progress = 0

        isPlaying = true
        btnPlayPause.setImageResource(R.drawable.pause)
        handler.post(miseAJourBarreDeProgression)
    }

    private fun jouerSuivante() {
        currentSongIndex = (currentSongIndex + 1) % chansonsDuGenre.size
        chansonActuelle = chansonsDuGenre[currentSongIndex]
        jouerChanson(chansonActuelle)
    }

    private fun setupListeners() {
        btnPlaylist.setOnClickListener {
            afficherListeDeLecturePourAjout()
        }

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

        progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Arrêter la mise à jour automatique pendant le déplacement de la barre
                handler.removeCallbacks(miseAJourBarreDeProgression)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Redémarrer la mise à jour de la barre une fois le déplacement terminé
                handler.post(miseAJourBarreDeProgression)
            }
        })

        btnPrevious.setOnClickListener {
            currentSongIndex = if (currentSongIndex > 0) currentSongIndex - 1 else chansonsDuGenre.size - 1
            chansonActuelle = chansonsDuGenre[currentSongIndex]
            jouerChanson(chansonActuelle)
        }
        btnNext.setOnClickListener { jouerSuivante() }
        btnShuffle.setOnClickListener {
            chansonsDuGenre = chansonsDuGenre.shuffled()
            currentSongIndex = 0
            chansonActuelle = chansonsDuGenre[currentSongIndex]
            jouerChanson(chansonActuelle)
        }

        btnRepeat.setOnClickListener {
            isRepeatEnabled = !isRepeatEnabled
            btnRepeat.setImageResource(if (isRepeatEnabled) R.drawable.repeat_active else R.drawable.repeat)
        }
        btnArrowDown.setOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }

        btnFavorite.setOnClickListener {
            présentateur.obtenirFavoris { favoris ->
                val chansonDansFavoris = favoris?.chansons?.contains(chansonActuelle) == true
                if (chansonDansFavoris) {
                    Toast.makeText(requireContext(), "Chanson déjà dans les favoris", Toast.LENGTH_SHORT).show()
                } else {
                    présentateur.ajouterAuxFavoris(chansonActuelle)
                    Toast.makeText(requireContext(), "Chanson ajoutée aux favoris", Toast.LENGTH_SHORT).show()
                    updateFavoriteButtonIcon()
                }
            }
        }
    }

    private fun updateFavoriteButtonIcon() {
        présentateur.obtenirFavoris { favoris ->
            val chansonDansFavoris = favoris?.chansons?.contains(chansonActuelle) == true
            btnFavorite.setImageResource(
                if (chansonDansFavoris) R.drawable.favorited else R.drawable.favorite
            )
        }
    }

    private fun afficherListeDeLecturePourAjout() {
        val playlists = chansonSourceBidon.obtenirToutesLesListesDeLecture()
        if (playlists.isEmpty()) {
            Toast.makeText(requireContext(), "Aucune playlist disponible.", Toast.LENGTH_SHORT).show()
            return
        }

        val nomsPlaylists = playlists.map { it.nom }.toTypedArray()
        AlertDialog.Builder(requireContext())
            .setTitle("Ajouter à une playlist")
            .setItems(nomsPlaylists) { _, which ->
                val playlistSelectionnee = playlists[which]
                chansonSourceBidon.ajouterChansonALaPlaylist(playlistSelectionnee.id, chansonActuelle)
                Toast.makeText(requireContext(), "Chanson ajoutée à la playlist ${playlistSelectionnee.nom}.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.release()
        handler.removeCallbacks(miseAJourBarreDeProgression)
    }
}