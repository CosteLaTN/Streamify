package com.example.streamifymvp

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.NavController
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Presentation.Lecture.MiniPlayerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Récupérer le NavHostFragment et son NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        val navController = navHostFragment?.navController

        if (navController != null) {
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
            bottomNavigationView.setupWithNavController(navController)

            // Ajouter l'écouteur pour les événements de navigation
            bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.nav_home -> {
                        Log.d("MainActivity", "Navigating to Home")
                        navController.navigate(R.id.ecranAccueil)
                        true
                    }
                    R.id.nav_library -> {
                        Log.d("MainActivity", "Navigating to Library")
                        navController.navigate(R.id.ecranListeDeLecture)
                        true
                    }
                    R.id.nav_profile -> {
                        Log.d("MainActivity", "Navigating to Profile")
                        navController.navigate(R.id.profilVue)
                        true
                    }
                    R.id.nav_search -> {
                        Log.d("MainActivity", "Navigating to Search")
                        navController.navigate(R.id.fragmentEcranRecherche)
                        true
                    }
                    else -> false
                }
            }

            // Ajouter un écouteur pour détecter les changements de destination
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.ecranLecture) {
                    // Masquer le BottomNavigationView lorsque sur EcranLecture
                    bottomNavigationView.visibility = View.GONE
                } else {
                    // Afficher le BottomNavigationView pour toutes les autres destinations
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        } else {
            Log.e("MainActivity", "NavController introuvable")
        }

        Log.d("MainActivity", "onCreate: Activity initialisée")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Essayez de déplacer certaines transactions ici après que l’activité soit complètement initialisée
        supportFragmentManager.addOnBackStackChangedListener {
            Log.d("MainActivity", "BackStack Changed: Taille du BackStack: ${supportFragmentManager.backStackEntryCount}")
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume: Activity est visible")
    }

    fun ajouterMiniPlayerFragment(mediaPlayer: MediaPlayer, chansonActuelle: Chanson) {
        val miniPlayerFragment = MiniPlayerFragment(mediaPlayer, chansonActuelle, R.id.action_miniPlayerFragment_to_ecranLecture)

        val miniPlayerContainer = findViewById<FrameLayout>(R.id.miniPlayerContainer)
        if (miniPlayerContainer != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.miniPlayerContainer, miniPlayerFragment)
                .commitNow()

            miniPlayerContainer.visibility = View.VISIBLE
            Log.d("MainActivity", "MiniPlayerFragment ajouté avec succès")
        } else {
            Log.d("MainActivity", "Erreur: miniPlayerContainer non disponible")
            Toast.makeText(this, "MiniPlayerContainer non disponible", Toast.LENGTH_SHORT).show()
        }
    }

    fun isMiniPlayerActive(): Boolean {
        val fragment = supportFragmentManager.findFragmentById(R.id.miniPlayerContainer)
        return fragment is MiniPlayerFragment
    }
}
