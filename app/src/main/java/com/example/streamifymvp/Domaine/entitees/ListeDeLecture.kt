package com.example.streamifymvp.Domaine.entitees

data class ListeDeLecture(
    val id: Int,
    val nom: String,
    val chansons: MutableList<Chanson> = mutableListOf(),
)
