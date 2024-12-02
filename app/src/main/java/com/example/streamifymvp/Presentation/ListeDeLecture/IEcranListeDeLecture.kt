package com.example.streamifymvp.Presentation.ListeDeLecture

import com.example.streamifymvp.Domaine.entitees.ListeDeLecture

interface IEcranListeDeLecture {
    fun rafraichirListeDeLecture(playlists: List<ListeDeLecture>)
    fun afficherMessageSucces(message: String)
    fun afficherMessageErreur(message: String)
    fun afficherDialogCreationPlaylist()
}
