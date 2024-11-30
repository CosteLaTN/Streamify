package com.example.streamifymvp.Presentation.showDates
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streamifymvp.Domaine.entitees.ShowDate
import com.example.streamifymvp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class ShowDatesVue : Fragment(), ContratVuePrésentateurShowDates.IShowDatesVue {

    private lateinit var presentateur: ContratVuePrésentateurShowDates.IShowDatesPrésentateur
    lateinit var navController: NavController
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShowDatesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_dates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerView = view.findViewById(R.id.recyclerViewShowDates)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        presentateur = ShowDatesPrésentateur(this)


        adapter = ShowDatesAdapter(emptyList()) { showDate ->
            ouvrirCalendrier(showDate)
        }
        recyclerView.adapter = adapter


        presentateur.chargerDates()


        navController = findNavController()
        val bottomNavigationView: BottomNavigationView = requireView().findViewById(R.id.bottomNavigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    Log.d("ShowDatesVue", "Navigating to Home")
                    navController.navigate(R.id.ecranAccueil)
                    true
                }
                R.id.nav_library -> {
                    Log.d("ShowDatesVue", "Navigating to Library")
                    navController.navigate(R.id.action_showDatesVue_to_ecranListeDeLecture)
                    true
                }
                R.id.nav_profile -> {
                    Log.d("ShowDatesVue", "Navigating to Profile")
                    navController.navigate(R.id.action_showDatesVue_to_profilVue)
                    true
                }
                else -> false
            }
        }
    }

    override fun afficherDates(dates: List<ShowDate>) {

        adapter.updateDates(dates)
    }

    override fun afficherMessageErreur(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun ouvrirCalendrier(showDate: ShowDate) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, showDate.title)
            putExtra(CalendarContract.Events.DESCRIPTION, showDate.details)
            putExtra(CalendarContract.Events.EVENT_LOCATION, showDate.location)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, showDate.date.time)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, showDate.date.time + 2 * 60 * 60 * 1000) // Durée de 2 heures par défaut
        }

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "Aucune application de calendrier trouvée.", Toast.LENGTH_SHORT).show()
        }
    }
}
