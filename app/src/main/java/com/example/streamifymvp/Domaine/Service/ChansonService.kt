package com.example.streamifymvp.Domaine.Service

import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.SourceDeDonnees.ISourceDeDonnee
import com.example.streamifymvp.SourceDeDonnees.SourceDeDonneeBidon
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class ChansonService(private val source: ISourceDeDonnee = SourceDeDonneeBidon.instance) {

    suspend fun obtenirToutesLesChansons(): List<Chanson> {
        return source.obtenirToutesLesChansons()
    }

    suspend fun obtenirNouveautés(): List<Chanson> {

        val limite = "2000-11-17"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateLimite = dateFormat.parse(limite)

        return source.obtenirToutesLesChansons().filter { chanson ->
            try {
                val dateChanson = dateFormat.parse(chanson.datePublication)
                val isAfter = dateChanson != null && dateChanson.after(dateLimite)
                isAfter
            } catch (e: ParseException) {
                false
            }
        }
    }


    suspend fun rechercherChansons(recherche: String): List<Chanson> {
        val rechercheMinuscule = recherche.lowercase()
        return source.obtenirToutesLesChansons().filter {
            it.nom.lowercase().contains(rechercheMinuscule) || it.genre.lowercase().contains(rechercheMinuscule) || it.artiste!!.pseudoArtiste.lowercase().contains(rechercheMinuscule)
        }
    }


    suspend fun ajouterAuxFavoris(chanson: Chanson) {
        source.ajouterAuxFavoris(chanson)
    }

}
