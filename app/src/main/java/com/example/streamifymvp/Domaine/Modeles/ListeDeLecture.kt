package com.example.streamifymvp.Domaine.Modeles

data class ListeDeLecture(
    val id: Int = 0,
    val nom: String,
    val chansons: MutableList<Chanson> = mutableListOf(),
)
