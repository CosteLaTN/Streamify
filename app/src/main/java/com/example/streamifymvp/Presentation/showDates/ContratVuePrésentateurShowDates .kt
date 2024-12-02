package com.example.streamifymvp.Presentation.showDates


import com.example.streamifymvp.Domaine.entitees.ShowDate

interface ContratVuePrésentateurShowDates {
    interface IShowDatesVue {
        fun afficherDates(dates: List<ShowDate>)
        fun afficherMessageErreur(message: String)
        fun ouvrirCalendrier(showDate: ShowDate)
    }

    interface IShowDatesPrésentateur {
        fun chargerDates()

    }
}
