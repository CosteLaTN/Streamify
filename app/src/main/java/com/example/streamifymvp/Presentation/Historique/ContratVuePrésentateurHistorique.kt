package com.example.streamifymvp.Presentation.Historique

interface ContratVuePrésentateurHistorique {
    interface IHistoriqueVue {
        fun afficherHistoriqueRecherche(historique: List<String>)
        fun afficherMessageErreur(message: String)
    }

    interface IHistoriquePrésentateur {
        fun chargerHistoriqueRecherche()
    }
}