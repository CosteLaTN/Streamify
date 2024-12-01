package com.example.streamifymvp.Presentation.Lecture

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.Service.ArtisteService
import com.example.streamifymvp.R
import com.example.streamifymvp.Presentation.Modele
import com.example.streamifymvp.Domaine.Service.ChansonService
import com.example.streamifymvp.Domaine.Service.ListeDeLectureService
import com.example.streamifymvp.MainActivity
import com.example.streamifymvp.Presentation.Lecture.MiniPlayerFragment
import com.example.streamifymvp.SourceDeDonnees.SourceDeDonneeBidon

class EcranLecture : Fragment(), IEcranLecture  {

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
    private val chansonSourceBidon = SourceDeDonneeBidon.instance
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

        val chansonService = ChansonService(SourceDeDonneeBidon.instance)
        val artisteService = ArtisteService(SourceDeDonneeBidon())
        val listeDeLectureService = ListeDeLectureService(SourceDeDonneeBidon.instance)
        val modele = Modele(chansonService, artisteService, listeDeLectureService)
        présentateur = LecturePresentateur(modele)

        val chansonId = arguments?.getInt("chansonId") ?: -1
        if (chansonId != -1) {
            chansonActuelle = chansonSourceBidon.obtenirToutesLesChansons().find { it.id == chansonId }
                ?: run {
                    Log.e("EcranLecture", "Chanson introuvable pour ID : $chansonId")
                    Toast.makeText(requireContext(), "Erreur : Chanson introuvable", Toast.LENGTH_SHORT).show()
                    return
                }

            chansonsDuGenre = chansonSourceBidon.obtenirToutesLesChansons().filter { it.genre == chansonActuelle.genre }
            currentSongIndex = chansonsDuGenre.indexOf(chansonActuelle)

            setupListeners()
            jouerChanson(chansonActuelle)
        } else {
            Log.e("EcranLecture", "ChansonId invalide : $chansonId")
            Toast.makeText(requireContext(), "Erreur : ChansonId invalide", Toast.LENGTH_SHORT).show()
        }
    }

    override fun jouerChanson(chanson: Chanson) {
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
        val artiste = chansonSourceBidon.obtenirArtisteParId(chanson.artisteId)
        artistName.text = artiste?.pseudoArtiste ?: "Artiste Inconnu"
        imageAlbum.setImageResource(chanson.imageChanson)

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

                handler.removeCallbacks(miseAJourBarreDeProgression)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

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
            val activity = requireActivity() as? MainActivity
            if (activity != null && mediaPlayer != null) {
                activity.ajouterMiniPlayerFragment(mediaPlayer!!, chansonActuelle)

                // Rendre la barre de lecture visible
                val miniPlayerContainer = activity.findViewById<FrameLayout>(R.id.miniPlayerContainer)
                miniPlayerContainer.visibility = View.VISIBLE

                // Mettre à jour la progress bar, le titre et l'artiste
                val fragment = childFragmentManager.findFragmentById(R.id.miniPlayerContainer) as? MiniPlayerFragment
                fragment?.apply {
                    songTitle.text = chansonActuelle.nom
                    albumArt.setImageResource(chansonActuelle.imageChanson)
                    progressBar.progress = mediaPlayer!!.currentPosition
                }

                // Retourner à l'écran d'accueil
                Navigation.findNavController(requireView()).popBackStack()
            } else {
                Log.d("EcranLecture", "Erreur: MainActivity ou mediaPlayer est null ou le media n'est pas prêt")
                Toast.makeText(requireContext(), "Erreur lors de l'ajout du MiniPlayer", Toast.LENGTH_SHORT).show()
            }
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

    override fun updateFavoriteButtonIcon() {
        présentateur.obtenirFavoris { favoris ->
            val chansonDansFavoris = favoris?.chansons?.contains(chansonActuelle) == true
            btnFavorite.setImageResource(
                if (chansonDansFavoris) R.drawable.favorited else R.drawable.favorite
            )
        }
    }

    override fun afficherListeDeLecturePourAjout() {
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
        // Ne libérez pas le MediaPlayer si on passe au MiniPlayerFragment
        val activity = requireActivity() as? MainActivity
        if (activity == null || !activity.isMiniPlayerActive()) {
            mediaPlayer?.release()
            handler.removeCallbacks(miseAJourBarreDeProgression)
        }
    }

}
