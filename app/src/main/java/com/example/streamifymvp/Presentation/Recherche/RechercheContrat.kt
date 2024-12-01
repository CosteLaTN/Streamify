package com.example.streamifymvp.Presentation.Recherche

import com.example.streamifymvp.Domaine.entitees.Chanson

interface RechercheContrat {

    interface IRechercheVue {

        fun afficherResultats(resultats: List<Chanson>)


        fun afficherMessageAucunResultat()
    }

    interface IRecherchePresentateur {

        fun effectuerRecherche(query: String)
    }
}
