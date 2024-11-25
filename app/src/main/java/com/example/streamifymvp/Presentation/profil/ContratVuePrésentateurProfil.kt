interface ContratVuePrésentateurProfil {

    interface IProfilVue {
        fun afficherNomUtilisateur(nom: String)
        fun afficherUsername(username: String)
        fun afficherMessageErreur(message: String)
        fun afficherMessageSucces(message: String)
        fun changerLangue(languageCode: String)
        fun afficherDialogueModificationNom()
        fun afficherDialogueModificationUsername()
    }

    interface IProfilPrésentateur {
        fun chargerProfil()
        fun modifierNomUtilisateur(nouveauNom: String)
        fun modifierUsername(nouveauUsername: String)
        fun sauvegarderChansonDuJour(chanson: String)
        fun gererChangementLangue()
        fun gererModificationNomUtilisateur(nouveauNom: String)
        fun gererModificationUsername(nouveauUsername: String)
    }
}
