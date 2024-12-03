package com.example.streamifymvp.Domaine.entitees

data class Chanson(
    val id: Int,
    val nom: String,
    val datePublication: String,
    val genre: String,
    val dureeAlbum: String,
    val dureeMusique: String,
    val imageChanson: String,
    val fichierAudio: String,
    val albumId: Int?,
    val artiste: Artiste? // Inclure l'objet Artiste au lieu de artisteId
)
