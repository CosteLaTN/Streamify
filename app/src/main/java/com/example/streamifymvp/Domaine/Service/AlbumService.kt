/*package com.example.streamifymvp.Domaine.Service

import com.example.streamifymvp.Domaine.entitees.Album
import com.example.streamifymvp.Domaine.entitees.Chanson

class AlbumService(private val source: AlbumSourceDeDonnees) {

    // Obtenir tous les albums
    fun obtenirTousLesAlbums(): List<Album> {
        return source.obtenirTousLesAlbums()
    }

    fun obtenirAlbumParId(id: Int): Album? {
        return source.obtenirTousLesAlbums().find { it.id == id }
    }

    fun rechercherAlbums(recherche: String): List<Album> {
        val rechercheMinuscule = recherche.lowercase()
        return source.obtenirTousLesAlbums().filter {
            it.nom.lowercase().contains(rechercheMinuscule) || it.nomArtiste.lowercase().contains(rechercheMinuscule)
        }
    }

    fun obtenirChansonsParAlbum(idAlbum: Int): List<Chanson> {
        val album = source.obtenirTousLesAlbums().find { it.id == idAlbum }
        return album?.chansons ?: emptyList()
    }
}
*/