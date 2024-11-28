import com.example.streamifymvp.Domaine.Modeles.ShowDate

interface ContratVuePrésentateurShowDates {
    interface IShowDatesVue {
        fun afficherDates(dates: List<ShowDate>)
        fun afficherMessageErreur(message: String)
    }

    interface IShowDatesPrésentateur {
        fun chargerDates()
    }
}
