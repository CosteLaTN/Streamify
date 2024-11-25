package com.example.streamifymvp.Domaine.Modeles

data class Chanson(
    val id: Int,
    val nom: String,
    val datePublication: String,
    val genre: String,
    val dureeAlbum: String,
    val dureeMusique: String,
    val imageChanson: Int,
    val fichierAudio: Int,
    val albumId : Int?,
    val artisteId: Int)
