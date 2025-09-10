package com.example.firebnb.présentation.vue

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.firebnb.R
import com.example.firebnb.domaine.entité.Reservation
import com.example.firebnb.présentation.modèle.ModèleImpl
import com.example.firebnb.présentation.présentateur.DetailReservationVuePresentateur

class DetailReservationVue : Fragment(), ReservationDetailVue{
    private lateinit var presentateur: DetailReservationVuePresentateur
    private var reservationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presentateur = DetailReservationVuePresentateur(this,requireContext())

        reservationId = arguments?.getString("RESERVATION_ID")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reservationId?.let {
            presentateur.chargerDetailsReservation(it)
        } ?: run {
            afficherErreur(getString(R.string.IdNonFourni))
        }

        initialiserVue(view)
    }

    private fun initialiserVue(vue: View) {
        vue.findViewById<Button>(R.id.AnnulerReservationButton).setOnClickListener {
            reservationId?.let { id ->
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.Confirmation_annulation_title))
                    .setMessage(getString(R.string.Confirmation_annulation_msg))
                    .setPositiveButton(getString(R.string.oui)) { _, _ ->
                        presentateur.supprimerReservation(id)
                    }
                    .setNegativeButton(getString(R.string.non)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            } ?: afficherErreur(getString(R.string.ReservationNonExistant))
        }
    }

    override fun afficherDetails(reservation: Reservation) {
        view?.let { vue ->
            vue.findViewById<TextView>(R.id.propertyTitle).text = reservation.titre
            vue.findViewById<TextView>(R.id.propertyPrix).text = "${reservation.prix} $"
            vue.findViewById<TextView>(R.id.locationText).text = "${reservation.ville}, ${reservation.pays}"
            vue.findViewById<TextView>(R.id.dateArrivee).text = "Date d'arrivée : ${reservation.dateArrivee} Date de sortie: ${reservation.dateSortie}"
            vue.findViewById<TextView>(R.id.dateDepart).text = "État : ${reservation.status}"

            val imageView = vue.findViewById<ImageView>(R.id.propertyImage)
            val imageResId = resources.getIdentifier(reservation.imageUrl, "drawable", requireContext().packageName)
            if (imageResId != 0) {
                imageView.setImageResource(imageResId)
            } else {
                imageView.setImageResource(R.drawable.image_par_defaut)
            }
        }
    }

    override fun afficherErreur(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun afficherMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}