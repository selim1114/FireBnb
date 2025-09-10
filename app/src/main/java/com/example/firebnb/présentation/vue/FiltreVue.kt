package com.example.firebnb.présentation.vue

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.firebnb.R
import com.example.firebnb.domaine.entité.FiltreClass
import com.example.firebnb.présentation.ProprieteDTO
import com.example.firebnb.présentation.présentateur.FiltreVuePrésentateur
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Date
import java.util.Locale

class FiltreVue : Fragment() {
    private lateinit var navController: NavController
    private lateinit var presentateur: FiltreVuePrésentateur
    private var filtre: FiltreClass? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presentateur = FiltreVuePrésentateur(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filtre_vue, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initializeUI(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeUI(view: View) {
        val destinationInput = view.findViewById<EditText>(R.id.destinationInput)
        val roomCountInput = view.findViewById<EditText>(R.id.peopleInput)
        val dateRangeText = view.findViewById<TextView>(R.id.dateIntervalle)
        val priceSeekBar = view.findViewById<SeekBar>(R.id.priceSeekBar)
        val priceRangeText = view.findViewById<TextView>(R.id.priceRangeText)

        var maxPrix = 0.0
        var dateArrivée: Long? = null
        var dateDépart: Long? = null

        // Configurer le SeekBar
        priceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                maxPrix = progress.toDouble()
                priceRangeText.text = "$0 - $$maxPrix"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Configurer le sélecteur de dates
        view.findViewById<Button>(R.id.btnSelectDates).setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Sélectionner une plage de dates")
                .build()

            datePicker.show(parentFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener { selection ->
                dateArrivée = selection.first
                dateDépart = selection.second
                dateRangeText.text = "${formatDate(dateArrivée)} - ${formatDate(dateDépart)}"
            }
        }

        // Appliquer le filtre
        view.findViewById<Button>(R.id.searchButton).setOnClickListener {
            filtre = FiltreClass(
                destination = destinationInput.text.toString(),
                minPrix = 0.0,
                maxPrix = maxPrix,
                nbChambre = roomCountInput.text.toString().toIntOrNull(),
                dateArrivée = dateArrivée,
                dateDeparture = dateDépart
            )
            presentateur.appliquerFiltre(filtre!!)
        }
    }

    private fun formatDate(date: Long?): String {
        return if (date != null) {
            java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(date))
        } else {
            "Non spécifié"
        }
    }

    fun naviguerAvecRésultats(propriétésDTO: List<ProprieteDTO>) {
        if (propriétésDTO.isEmpty()) {
            afficherErreur("Aucune propriété trouvée avec ces critères.")
            return
        }

        val bundle = Bundle().apply {
            putParcelableArrayList("resultats", ArrayList(propriétésDTO))
        }

        Log.d("FiltreVue", "Navigation avec résultats : $propriétésDTO")
        navController.navigate(R.id.action_filtre_to_accueil, bundle)
    }


    fun afficherErreur(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


}

