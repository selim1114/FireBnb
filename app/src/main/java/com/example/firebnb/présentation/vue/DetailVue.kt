package com.example.firebnb.présentation.vue

import android.app.AlertDialog
import android.icu.text.NumberFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.DateInterval
import android.icu.util.TimeZone
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.firebnb.R
import com.example.firebnb.présentation.ProprieteDTO
import com.example.firebnb.présentation.présentateur.DetailsPropriétéVuePresentateur
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Date
import java.util.Locale

class DetailVue : Fragment(), OnMapReadyCallback {
    lateinit var presentateur: DetailsPropriétéVuePresentateur
    private lateinit var carte: MapView
    private var propriete: ProprieteDTO? = null
    lateinit var navControlleur: NavController
    lateinit var listDateContrainte : List<DateInterval>
    private lateinit var btnSelectDates: ImageButton
    lateinit var dateChoisie: TextView
    private lateinit var btnLike: ImageButton
    private var dateArrivee: Long? = null
    private var dateDepart: Long? = null
    private var estAimé : Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_propriete_vue, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presentateur = DetailsPropriétéVuePresentateur(this, requireContext())
        initialiserView(view)
        initializeMap(savedInstanceState)
        btnLike = view.findViewById(R.id.like)
        navControlleur = Navigation.findNavController(view)
        btnSelectDates = requireActivity().findViewById(R.id.btnSelectDates)
        dateChoisie = requireActivity().findViewById(R.id.dateChoisie)
        btnSelectDates.setOnClickListener() { selectDates() }

        btnLike.setOnClickListener {
            propriete?.let { proprieteActuelle ->
                estAimé = if (estAimé) {
                    presentateur.supprimerFavoris(proprieteActuelle.id.toInt())
                    false
                } else {
                    presentateur.ajouterFavoris(proprieteActuelle)
                    true
                }
                mettreAJourBoutonLike(estAimé)
            }
        }
    }

    fun afficherDetails(propriete: ProprieteDTO) {
        this.propriete = propriete
        view?.let {
            updateProprieteDetails(it, propriete)
            estAimé = presentateur.estProprieteDansFavoris(propriete.id.toInt())
            mettreAJourBoutonLike(estAimé)
        }
    }

    private fun mettreAJourBoutonLike(estAimé: Boolean) {
        btnLike.setImageResource(if (estAimé) R.drawable.like3 else R.drawable.like)
    }

    private fun formatDate(date: Long): String {
        val formatter = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
        return formatter.format(java.util.Date(date))
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initialiserView(view: View) {
        carte = view.findViewById(R.id.mapView)
        view.findViewById<Button>(R.id.reserveButton).setOnClickListener {
            if (propriete != null && dateArrivee != null && dateDepart != null) {
                val message = "Voulez-vous vraiment réserver du ${formatDate(dateArrivee!!)} au ${formatDate(dateDepart!!)} ?"

                AlertDialog.Builder(requireContext())
                    .setTitle("Confirmation de réservation")
                    .setMessage(message)
                    .setPositiveButton("Confirmer") { _, _ ->
                        presentateur.onReserveNowClicked(propriete!!, dateArrivee!!, dateDepart!!)
                    }
                    .setNegativeButton("Annuler") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            } else {
                afficherErreur("Veuillez sélectionner des dates avant de réserver.")
            }
        }
        view.findViewById<ImageView>(R.id.backArrow).setOnClickListener {
            presentateur.navigationEnArrier()
        }
        setHasOptionsMenu(true)

        presentateur.rechercherPropriétéSélectionnée()
    }

    private fun initializeMap(savedInstanceState: Bundle?) {
        carte.onCreate(savedInstanceState)
        carte.getMapAsync(this)
    }

    private fun getFormattedCurrency(price: Double): String {
        val locale = Locale.getDefault()
        val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
        return currencyFormatter.format(price)
    }

    private fun updateProprieteDetails(view: View, propriete: ProprieteDTO) {

        view.findViewById<TextView>(R.id.propertyTitle).text = propriete.titre
        view.findViewById<TextView>(R.id.locationText).text = propriete.adresse
        view.findViewById<TextView>(R.id.propertyDescription).text = propriete.description
        view.findViewById<TextView>(R.id.confortText).text = propriete.equipements
        view.findViewById<TextView>(R.id.nombre_de_chambre).text = propriete.nbChambre
        view.findViewById<TextView>(R.id.priceText).text = getString(R.string.PrixParNuit, getFormattedCurrency(propriete.prix.toDouble()))
        view.findViewById<TextView>(R.id.capacite_maximale).text = propriete.capaciteMax
        view.findViewById<TextView>(R.id.nombre_de_salles_de_bain).text = propriete.nbSallesDeBain
        val proprietePhoto = view.findViewById<ImageView>(R.id.propertyImage)
        val resId = view.context.resources.getIdentifier(
            propriete.photo,
            "drawable",
            view.context.packageName
        )

       proprietePhoto.setImageResource(
           if (resId !=0) resId else R.drawable.image_par_defaut
       )

       // val proprietePhoto = view.findViewById<ImageView>(R.id.propertyImage)
       // Glide.with(view.context)
        //    .load(propriete.photo)
         //   .apply(RequestOptions().placeholder(R.drawable.image_par_defaut).error(R.drawable.image_par_defaut))
         //   .into(proprietePhoto)
        presentateur.getlisteDateContrainte(propriete.dateDisponible)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        propriete?.let {
            val location = LatLng(it.latitude.toDouble(), it.longitude.toDouble())
            googleMap.addMarker(MarkerOptions().position(location).title("Location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
        }
    }

    fun afficherErreur(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    fun afficherMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    //pour la fonction de contrainte: https://stackoverflow.com/questions/62469312/materialdatepicker-select-date-between-2-dates-only
    private fun calendarConstraints(): CalendarConstraints {

        val listRangeLimits = listDateContrainte.map { dateInterval ->

            //on combine les deux dates pour une limites
            CompositeDateValidator.allOf(
                listOf(
                    //inclue jour arrivé
                    DateValidatorPointForward.from(dateInterval.fromDate-86400000),
                    DateValidatorPointBackward.before(dateInterval.toDate)

                )
            )
        }

        //est valide si elle satisfie au moins une dateInterval
        val validators = CompositeDateValidator.anyOf(listRangeLimits)

        return CalendarConstraints.Builder()
            .setValidator(validators)
            .build()
    }

    fun selectDates() {

        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setCalendarConstraints(calendarConstraints())
        .setTitleText("Sélectionner une date d'arrivé et de départ")
            .setSelection(
                Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            ).build()
        picker.show(this.parentFragmentManager, "TAG")
        picker.addOnPositiveButtonClickListener { selection ->
            var dateArrivée = selection.first
            var dateDépart = selection.second

            val timeZoneOffset = TimeZone.getDefault().getOffset(dateArrivée)
            dateArrivée -= timeZoneOffset
            dateDépart -= timeZoneOffset

            val dateFormatter = SimpleDateFormat("d - MMM", Locale.getDefault())
            val dateArrivéeFormaté = dateFormatter.format(Date(dateArrivée))
            val dateDépartFormaté = dateFormatter.format(Date(dateDépart))
            presentateur.afficherDateSélectionné(dateArrivéeFormaté,dateDépartFormaté)
            this.dateArrivee = dateArrivée
            this.dateDepart = dateDépart

            presentateur.ajouterAuCalendrier(DateInterval(dateArrivée,dateDépart))
        }
    }

    //Convertir Stringto en DateListe puis en un string qui est closest date
    fun convertDTODispoToDateArray(dateDisponible: String): ArrayList<DateInterval> {
        Log.d("dateDisponibles", "dateDisponible $dateDisponible")
        val dateFormat = java.text.SimpleDateFormat("dd MMM yyyy")
        val dispo = ArrayList<DateInterval>()
        if(dateDisponible.contains(",")) {
            dateDisponible.split(",").map { uneDispo ->
                val journee = uneDispo.split(" to ")
                dispo.add(DateInterval(dateFormat.parse(journee[0]).time, dateFormat.parse(journee[1]).time))
            }
            Log.d("listeDispo", "listeDispo $dispo")
            return dispo
        }
        else if (dateDisponible.contains("to")){
            var journee = dateDisponible.split(" to ")
            dispo.add(DateInterval(dateFormat.parse(journee[0]).time, dateFormat.parse(journee[1]).time))
            Log.d("listeDispo", "listeDispo $dispo")
            return dispo
        }
        else return ArrayList()
    }

    override fun onResume() {
        super.onResume()
        carte.onResume()
    }

    override fun onPause() {
        super.onPause()
        carte.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        carte.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        carte.onLowMemory()
    }

    fun navigation() {
        findNavController().navigate(R.id.action_propriete_to_accueil)
    }
}