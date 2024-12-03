package com.example.streamifymvp

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Presentation.Lecture.MiniPlayerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        val navController = navHostFragment?.navController

        if (navController != null) {
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
            bottomNavigationView.setupWithNavController(navController)

            bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.nav_home -> {
                        navController.navigate(R.id.ecranAccueil)
                        true
                    }
                    R.id.nav_library -> {
                        navController.navigate(R.id.ecranListeDeLecture)
                        true
                    }
                    R.id.nav_profile -> {
                        navController.navigate(R.id.profilVue)
                        true
                    }
                    R.id.nav_search -> {
                        navController.navigate(R.id.fragmentEcranRecherche)
                        true
                    }
                    else -> false
                }
            }

            navController.addOnDestinationChangedListener { _, destination, _ ->
                val bottomNavigationViewVisibility = if (destination.id == R.id.ecranLecture) View.GONE else View.VISIBLE
                bottomNavigationView.visibility = bottomNavigationViewVisibility
            }
        }
    }

    fun ajouterMiniPlayerFragment(mediaPlayer: MediaPlayer?, chansonActuelle: Chanson) {
        if (mediaPlayer == null) {
            Log.e("MainActivity", "Erreur: MediaPlayer est null")
            Toast.makeText(this, "Impossible d'ajouter le MiniPlayer", Toast.LENGTH_SHORT).show()
            return
        }

        val miniPlayerFragment = MiniPlayerFragment(chansonActuelle, R.id.ecranLecture)
        val miniPlayerContainer = findViewById<FrameLayout>(R.id.miniPlayerContainer)

        supportFragmentManager.beginTransaction()
            .replace(R.id.miniPlayerContainer, miniPlayerFragment)
            .commitNow()

        miniPlayerContainer.visibility = View.VISIBLE
    }

}


