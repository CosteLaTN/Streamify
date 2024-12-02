package com.example.streamifymvp.Presentation.ListeDeLecture

interface IEcranListeDeLecturePresentateur {
    fun chargerListesDeLecture()
    fun ajouterNouvellePlaylist(nom: String)
    fun onDestroy()
}
