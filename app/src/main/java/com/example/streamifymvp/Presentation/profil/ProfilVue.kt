package com.example.streamifymvp.Presentation.profil


import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.streamifymvp.R
import com.example.streamifymvp.SourceDeDonnees.SourceDeDonneeBidon
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

class ProfilVue : Fragment(), ContratVuePrésentateurProfil.IProfilVue {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_CAMERA_PERMISSION = 2
    }

    private lateinit var presentateur: ContratVuePrésentateurProfil.IProfilPrésentateur
    private lateinit var nameTextView: TextView
    private lateinit var usernameTextView: TextView
    private lateinit var profileImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ecran_profil, container, false)

        nameTextView = view.findViewById(R.id.name_profile)
        usernameTextView = view.findViewById(R.id.username_profile)
        profileImageView = view.findViewById(R.id.profilepic_profile)

        val service = SourceDeDonneeBidon()
        presentateur = ProfilPrésentateur(this, service = service)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presentateur.chargerProfil()

        lateinit var navController: NavController
        navController = findNavController()


        profileImageView.setOnClickListener {
            ouvrirCamera()
        }

        view.findViewById<Button>(R.id.edit_profile_button_profile).setOnClickListener {
            afficherDialogueModificationNom()
        }

        view.findViewById<Button>(R.id.add_status_button_profile).setOnClickListener {
            val input = EditText(requireContext()).apply {
                hint = "Chanson du jour!"
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Veuillez ajouter votre chanson du jour!")
                .setView(input)
                .setPositiveButton("Sauvegarder") { _, _ ->
                    val songOfTheDay = input.text.toString()
                    if (songOfTheDay.isNotBlank()) {
                        presentateur.sauvegarderChansonDuJour(songOfTheDay)
                    } else {
                        afficherMessageErreur("La chanson du jour ne peut pas être vide.")
                    }
                }
                .setNegativeButton("Annuler", null)
                .create()
                .show()
        }

        val boutonHistorique: LinearLayout = view.findViewById(R.id.btnHistoriqueProfil)
        boutonHistorique.setOnClickListener {
            navController.navigate(R.id.action_profilVue_to_historiqueVue)
        }

        val boutonRappel: LinearLayout = view.findViewById(R.id.btnRappel_profile)
        boutonRappel.setOnClickListener {
            navController.navigate(R.id.action_profilVue_to_showDatesVue)
        }
    }

    private fun ouvrirCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            lancerCamera()
        }
    }

    private fun lancerCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        } else {
            afficherMessageErreur("Impossible d'ouvrir la caméra.")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                lancerCamera()
            } else {
                afficherMessageErreur("Permission caméra refusée.")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == android.app.Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            profileImageView.setImageBitmap(imageBitmap)
        }
    }

    override fun afficherNomUtilisateur(nom: String) {
        nameTextView.text = nom
    }

    override fun afficherUsername(username: String) {
        usernameTextView.text = username
    }

    override fun afficherMessageErreur(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun afficherMessageSucces(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun changerLangue(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        requireActivity().recreate()
    }

    override fun afficherDialogueModificationNom() {
        val nameInput = EditText(requireContext()).apply {
            hint = "Nouveau nom d'affichage"
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Veuillez entrer un nouveau nom d'affichage")
            .setView(nameInput)
            .setPositiveButton("Confirmer") { _, _ ->
                val newName = nameInput.text.toString()
                if (newName.isNotBlank()) {
                    presentateur.gererModificationNomUtilisateur(newName)
                } else {
                    afficherMessageErreur("Le nom ne peut pas être vide")
                }
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    override fun afficherDialogueModificationUsername() {
        val usernameInput = EditText(requireContext()).apply {
            hint = "Nouveau nom d'utilisateur"
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Veuillez entrer un nouveau nom d'utilisateur")
            .setView(usernameInput)
            .setPositiveButton("Confirmer") { _, _ ->
                val newUsername = usernameInput.text.toString()
                if (newUsername.isNotBlank()) {
                    presentateur.gererModificationUsername(newUsername)
                } else {
                    afficherMessageErreur("Le nom d'utilisateur ne peut pas être vide")
                }
            }
            .setNegativeButton("Annuler", null)
            .show()
    }
}
