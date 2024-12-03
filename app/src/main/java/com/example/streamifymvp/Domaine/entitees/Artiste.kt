package com.example.streamifymvp.Domaine.entitees

data class Artiste(
    val id: Int,
    val nom: String,
    val prenom: String,
    val pseudoArtiste: String,
    val imageArtiste: String,
    val listeLectures: MutableList<ListeDeLecture> = mutableListOf()
)
