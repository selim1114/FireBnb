package com.example.firebnb.présentation.présentateur

import android.os.Bundle
import android.util.Log
import com.example.firebnb.domaine.entité.Reservation
import com.example.firebnb.présentation.modèle.Modèle
import com.example.firebnb.présentation.modèle.ModèleImpl
import com.example.firebnb.présentation.vue.ReservationVue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RéservationPrésentateurImpl(
    private val vue: ReservationVue
) : RéservationPrésentateur {
    var modele = ModèleImpl.instance
    private val reservationsList = mutableListOf<Reservation>()

    override fun chargerHistoriqueRéservations() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val reservations = modele.obtenirHistoriqueRéservations()
                reservationsList.clear()
                reservationsList.addAll(reservations)
                withContext(Dispatchers.Main) {
                    vue.afficherRéservations(reservationsList)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    vue.afficherErreur("Erreur lors du chargement des réservations : ${e.message}")
                }
            }
        }
    }
    override fun chargerRéservationsEnCours() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val reservations = modele.obtenirRéservationsEnCours()
                reservationsList.clear()
                reservationsList.addAll(reservations)
                withContext(Dispatchers.Main) {
                    vue.afficherRéservations(reservationsList)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    vue.afficherErreur("Erreur lors du chargement des réservations en cours : ${e.message}")
                    Log.d("reservations presentateur", "${e.message}")
                }
            }
        }
    }
    override fun gérerClicRéservation(position: Int) {
        val reservation = reservationsList[position]
        val bundle = Bundle().apply {
            putInt("RESERVATION_ID", reservation.id)
            putString("TITRE", reservation.titre)
            putString("DATE_ARRIVEE", reservation.dateArrivee.toString())
            putString("DATE_SORTIE", reservation.dateSortie.toString())
            putString("DATE_RESERVATION", reservation.dateReservation.toString())
            putDouble("PRIX", reservation.prix)
            putString("STATUS", reservation.status.name)
            putString("PHOTO", reservation.imageUrl)
            putString("VILLE", reservation.ville)
            putString("PAYS", reservation.pays)
        }
        vue.naviguerVersDétails(bundle)
    }

    override fun ajouterRéservation(reservation: Reservation) {
        reservationsList.add(0, reservation)
        vue.notifierItemInséré(0)
    }

    override fun supprimerRéservation(position: Int) {
        if (position in reservationsList.indices) {
            reservationsList.removeAt(position)
            vue.notifierItemSupprimé(position)
        }
    }

    override fun obtenirNombreRéservations(): Int {
        return reservationsList.size
    }

    override fun obtenirRéservation(position: Int): Reservation {
        return reservationsList[position]
    }
}