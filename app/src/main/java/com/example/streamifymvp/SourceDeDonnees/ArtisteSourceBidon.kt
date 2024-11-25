package com.example.streamifymvp.SourceDeDonnees

import com.example.streamifymvp.Domaine.Modeles.Artiste
import com.example.streamifymvp.R

class ArtisteSourceBidon : ArtisteSourceDeDonnees {

    private val artistes = listOf(
        Artiste(
            id = 1,
            prenom = "Axl",
            nom = "Rose",
            pseudoArtiste = "Guns N' Roses",
            imageArtiste = R.drawable.cry
        ),
        Artiste(
            id = 2,
            prenom = "will.i.am",
            nom = "",
            pseudoArtiste = "The Black Eyed Peas",
            imageArtiste = R.drawable.humps
        ),
        Artiste(
            id = 3,
            prenom = "Guy-Manuel de Homem-Christo",
            nom = "& Thomas Bangalter",
            pseudoArtiste = "Daft Punk",
            imageArtiste = R.drawable.random_access_memories
        )
    )


    override fun obtenirTousLesArtistes(): List<Artiste> = artistes

    override fun obtenirArtisteParId(id: Int): Artiste? {
        return artistes.find { it.id == id }
    }

    override fun rechercherArtistes(recherche: String): List<Artiste> {
        val rechercheMinuscule = recherche.lowercase()
        return artistes.filter {
            it.pseudoArtiste.lowercase().contains(rechercheMinuscule)
        }
    }
}
