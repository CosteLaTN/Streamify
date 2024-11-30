package com.example.streamifymvp.Domaine.entitees

data class Artiste(
    val id: Int = 0,
    val nom: String,
    val prenom: String,
    val pseudoArtiste: String,
    val imageArtiste: Int,
    val listeLectures: MutableList<ListeDeLecture> = mutableListOf()
)
