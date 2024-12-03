package com.example.streamifymvp.Domaine.entitees

import java.time.LocalDate

data class Album(
    val id: Int,
    val nom: String,
    val chansons: MutableList<Chanson> = mutableListOf(),
    val datePublication: LocalDate,
    val dureeAlbum: String,
    val imageAlbum: Int,
    val nomArtiste: Artiste
     )
