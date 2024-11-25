package com.example.streamifymvp.SourceDeDonnees

import Profil
import java.text.SimpleDateFormat
import com.example.streamifymvp.Domaine.Modeles.ShowDate
import java.util.Locale

class SourceDeDonneeBidon : ISourceDeDonnee {

    private var profil: Profil? = Profil("Jean Dupont", "jdupont") // Exemple de données

    override fun obtenirProfil(): Profil? {
        return profil
    }

    override fun modifierNomUtilisateur(nouveauNom: String): Boolean {
        return if (profil != null) {
            profil!!.nom = nouveauNom
            true
        } else {
            false
        }
    }

    override fun modifierUsername(nouveauUsername: String): Boolean {
        return if (profil != null) {
            profil!!.username = nouveauUsername
            true
        } else {
            false
        }
    }

    private val historique = listOf("Daft Punk", "Discovery", "League of legends")

    override fun obtenirHistoriqueRecherche(): List<String> {
        return historique
    }

    private val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val datesDisponibles = listOf(
        ShowDate(
            title = "Concert de Daft Punk",
            details = "Un concert légendaire de Daft Punk",
            date = format.parse("01/01/2024")!!,
            location = "Paris, France"
        ),
        ShowDate(
            title = "Concert de Adele",
            details = "Performance live de Adele",
            date = format.parse("05/01/2024")!!,
            location = "Londres, UK"
        ),
        ShowDate(
            title = "Festival de musique électronique",
            details = "Festival avec de nombreux artistes de musique électronique",
            date = format.parse("10/01/2024")!!,
            location = "Berlin, Allemagne"
        )
    )

    override fun obtenirToutesLesDatesDeShow(): List<ShowDate> {
        return datesDisponibles
    }
}
